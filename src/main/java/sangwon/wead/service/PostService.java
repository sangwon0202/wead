package sangwon.wead.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.DTO.PostDto;
import sangwon.wead.DTO.PostFormDto;
import sangwon.wead.DTO.PostMetaDataDto;
import sangwon.wead.DTO.PageBarDto;
import sangwon.wead.entity.Post;
import sangwon.wead.repository.PostRepository;
import sangwon.wead.repository.CommentRepository;
import sangwon.wead.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public List<PostMetaDataDto> getListAll() {

        return postRepository.findAll().stream().map((post) -> {
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
        }).toList();
    }

    public List<PostMetaDataDto> getListPaging(int pageNumber, int postCountPerPage) {

        return postRepository.findPaging(pageNumber,postCountPerPage).stream().map((post) -> {
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
        }).toList();
    }

    public boolean isPageNumberValid(int pageNumber, int postCountPerPage) {
        if(pageNumber <= 0) return false;

        int postCount = postRepository.getCount();
        if(postCount == 0) return pageNumber == 1;

        int pageCount = (postCount-1)/postCountPerPage + 1;
        return pageNumber <= pageCount;
    }

    public PageBarDto getPageBar(int pageNumber, int postCountPerPage, int pageCountPerPageBar) {
        int postCount = postRepository.getCount();
        int pageCount = (postCount-1)/postCountPerPage + 1;
        int pageBarNumber = (pageNumber-1)/pageCountPerPageBar + 1;
        int pageBarCount = (pageCount-1)/pageCountPerPageBar + 1;

        PageBarDto pageBarDto = new PageBarDto();
        pageBarDto.setCurrent(pageNumber);
        pageBarDto.setPrev(pageBarNumber > 1);
        pageBarDto.setNext(pageBarNumber != pageBarCount);
        pageBarDto.setStart((pageBarNumber-1)*pageCountPerPageBar + 1);
        if(pageBarDto.isNext()) pageBarDto.setEnd(pageBarDto.getStart() + pageCountPerPageBar - 1);
        else pageBarDto.setEnd(pageCount);

        return pageBarDto;
    }

    public boolean postExist(int postId) {
        return postRepository.findByPostId(postId).isPresent();
    }

    public boolean isWriter(String userId, int postId) {
        return postRepository.findByPostId(postId).get().getUserId().equals(userId);
    }

    public PostDto read(int postId) {

        Post post = postRepository.findByPostId(postId).get();
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

    public void update(int postId, PostFormDto postFormDto) {
        Post post = postRepository.findByPostId(postId).get();
        post.setTitle(postFormDto.getTitle());
        post.setContent(postFormDto.getContent());

        postRepository.save(post);
    }

    public void delete(int postId) {
        postRepository.deleteByPostId(postId);
    }

    public void increaseViews(int postId) {
        Post post = postRepository.findByPostId(postId).get();
        post.setView(post.getView()+1);
        postRepository.save(post);
    }
}
