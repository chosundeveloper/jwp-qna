package qna.domain;

public class QuestionTest {
    public static final Question QUESTION_1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question QUESTION_2 = new Question(1L, "title2", "contents2").writeBy(UserTest.SANJIGI);
}
