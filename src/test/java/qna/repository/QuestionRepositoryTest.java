package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.ANSWERS_CONTENTS_2;
import static qna.domain.QuestionTest.QUESTION_1;
import static qna.domain.QuestionTest.QUESTION_2;

@DataJpaTest
@DisplayName("질문 Repository")
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("저장_성공")
    @Test
    void save() {

        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(createUser(UserTest.JAVAJIGI)));

        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getContents()).isEqualTo(QUESTION_1.getContents()),
                () -> assertThat(question.getTitle()).isEqualTo(QUESTION_1.getTitle()),
                () -> assertThat(question.isDeleted()).isFalse(),
                () -> assertThat(question.getWriter()).isNotNull(),
                () -> assertThat(question.getCreatedAt()).isNotNull(),
                () -> assertThat(question.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("findByDeletedFalse_조회_성공")
    @Test
    void findByDeletedFalse() {

        Question question1 = createQuestion(createUser(UserTest.JAVAJIGI), QUESTION_1);
        Question question2 = createQuestion(createUser(UserTest.SANJIGI), QUESTION_2);

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(questions).hasSize(2),
                () -> assertThat(questions).containsExactly(question1, question2)
        );
    }

    @DisplayName("findByIdAndDeletedFalse_조회_성공")
    @Test
    void findByIdAndDeletedFalse() {

        Question question = createQuestion(createUser(UserTest.JAVAJIGI), QUESTION_1);

        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getContents()).isEqualTo(QUESTION_1.getContents()),
                () -> assertThat(question.getTitle()).isEqualTo(QUESTION_1.getTitle()),
                () -> assertThat(question.isDeleted()).isFalse(),
                () -> assertThat(question.getWriter()).isNotNull(),
                () -> assertThat(question.getCreatedAt()).isNotNull(),
                () -> assertThat(question.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("삭제_성공")
    @Test
    void delete() {

        Question question = createQuestion(createUser(UserTest.JAVAJIGI), QUESTION_1);
        assertThat(question).isNotNull();

        question.setDeleted(true);

        questionRepository.save(question);

        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isNotPresent();
    }

    @DisplayName("답변이 있으면 삭제할 수 없다.")
    @Test
    void delete_fail_exist_question() {
        User javajigi = createUser(UserTest.JAVAJIGI);
        User sanjigi = createUser(UserTest.SANJIGI);
        Question question = createQuestion(javajigi, QUESTION_1);
        createAnswer(sanjigi, question);
        assertThat(question).isNotNull();
        assertThat(question.getAnswers()).hasSize(1);

        assertThatThrownBy(question::delete)
                .isInstanceOf(CannotDeleteException.class);
    }

    private User createUser(User user) {
        return userRepository.save(user);
    }

    private Question createQuestion(User user, Question question) {
        return questionRepository.save(question.writeBy(user));
    }

    private Answer createAnswer(User user, Question question) {
        Answer answer = answerRepository.save(new Answer(user, question, ANSWERS_CONTENTS_2));
        question.addAnswer(answer);
        return answer;
    }
}
