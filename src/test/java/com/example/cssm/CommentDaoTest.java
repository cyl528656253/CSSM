package com.example.cssm;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.example.cssm.domain.*;
import com.example.cssm.dao.*;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CssmApplication.class)

@MapperScan("com.cai.demo.dao")
public class CommentDaoTest {


    @Autowired
    private CommentDAO commentDAO;

    @Test
    public void addComment() throws Exception {
        for(int i=0;i<11;i++){

            for(int j = 0; j < 3;j++){
                Comment comment = new Comment();
                comment.setUserId( i +1);
                comment.setCreatedDate(new Date());
                comment.setStatus(0);
                comment.setContent("这里是一个评论啊！" + String.valueOf(j));
                comment.setEntityId(12+i);
                comment.setEntityType(EntityType.ENTITY_NEWS);
                commentDAO.addComment(comment);
            }

        }
    }

    @Test
    public void selectByEntity() throws Exception {
        List<Comment> comments = commentDAO.selectByEntity(13,EntityType.ENTITY_NEWS);

        for(Comment comment : comments){
            System.out.println(comment.toString());
        }
    }

    @Test
    public void getCommentCount() throws Exception {
        System.out.println(commentDAO.getCommentCount(13,EntityType.ENTITY_NEWS));
    }

}
