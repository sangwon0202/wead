package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.DTO.*;
import sangwon.wead.exception.NonexistentPageException;
import sangwon.wead.exception.NonexistentPostException;
import sangwon.wead.exception.PermissionException;
import sangwon.wead.entity.Post;
import sangwon.wead.repository.PostRepository;
import sangwon.wead.repository.CommentRepository;
import sangwon.wead.repository.UserRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public PostListDto getList(int pageNumber, int postCountPerPage, int pageCountPerPageBar) throws NonexistentPageException {

        int postCount = postRepository.getCount();
        if(postCount == 0 && pageNumber != 1) throw new NonexistentPageException();

        int pageCount = (postCount-1)/postCountPerPage + 1;
        if(pageNumber > pageCount) throw new NonexistentPageException();

        PostListDto postListDto = new PostListDto();

        postListDto.setPostMetaDataDtoList(postRepository.findAll(pageNumber,postCountPerPage).stream().map((post) -> {
            PostMetaDataDto metaDataDto = new PostMetaDataDto();

            // 메타데이터 형성
            metaDataDto.setUserId(post.getUserId());
            metaDataDto.setPostId(post.getPostId());
            metaDataDto.setTitle(post.getTitle());
            metaDataDto.setUploadDate(post.getUploadDate());
            metaDataDto.setNickname(userRepository.findByUserId(post.getUserId()).get().getNickname());
            metaDataDto.setCommentNumber(commentRepository.findAllByPostId(post.getPostId()).size());
            metaDataDto.setViews(post.getView());

            return metaDataDto;
        }).toList());

        int pageBarNumber = (pageNumber-1)/pageCountPerPageBar + 1;
        int pageBarCount = (pageCount-1)/pageCountPerPageBar + 1;

        PageBarDto pageBarDto = new PageBarDto();
        pageBarDto.setCurrent(pageNumber);
        pageBarDto.setPrev(pageBarNumber > 1);
        pageBarDto.setNext(pageBarNumber != pageBarCount);
        pageBarDto.setStart((pageBarNumber-1)*pageCountPerPageBar + 1);
        if(pageBarDto.isNext()) pageBarDto.setEnd(pageBarDto.getStart() + pageCountPerPageBar - 1);
        else pageBarDto.setEnd(pageCount);

        postListDto.setPageBarDto(pageBarDto);

        return postListDto;
    }


    public PostDto read(int postId) throws NonexistentPostException {

        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NonexistentPostException());
        PostDto postDto = new PostDto();

        postDto.setPostId(post.getPostId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setUserId(post.getUserId());
        postDto.setUploadDate(post.getUploadDate());
        postDto.setNickname(userRepository.findByUserId(post.getUserId()).get().getNickname());
        postDto.setViews(post.getView());

        return postDto;
    }

    public void create(String userId, PostFormDto postFormDto) {

        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(postFormDto.getTitle());
        post.setContent(postFormDto.getContent());
        post.setUploadDate(new Date());
        post.setView(0);

        postRepository.save(post);
    }

    public void permissionCheck(String userId, int postId) throws NonexistentPostException, PermissionException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NonexistentPostException());
        if(!post.getUserId().equals(userId)) throw new PermissionException();
    }

    public void update(int postId, PostFormDto postFormDto) throws NonexistentPostException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NonexistentPostException());
        post.setTitle(postFormDto.getTitle());
        post.setContent(postFormDto.getContent());

        postRepository.save(post);
    }

    public void delete(int postId) throws NonexistentPostException {
        if(postRepository.findByPostId(postId).isEmpty()) throw new NonexistentPostException();
        postRepository.deleteByPostId(postId);
    }

    public void increaseViews(int postId) throws NonexistentPostException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NonexistentPostException());
        post.setView(post.getView()+1);
        postRepository.save(post);
    }
}
