package ru.practicum.shareit.item.comment;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentMapper {


     public static CommentDto createCommentDto(Comment comment){
         CommentDto commentDto = new CommentDto();
         commentDto.setId(comment.getId());
         commentDto.setAuthorName(comment.getAuthor().getName());
         commentDto.setItemId(comment.getItemId());
         commentDto.setText(comment.getText());
         commentDto.setCreated(comment.getCreated());
         return commentDto;
     }
}
