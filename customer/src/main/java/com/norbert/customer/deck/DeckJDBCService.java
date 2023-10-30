package com.norbert.customer.deck;

import com.norbert.customer.user.User;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeckJDBCService implements DeckDAO{
    private final JdbcTemplate jdbcTemplate;
    @Override
    public void save(Deck deck) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sql =
                "INSERT INTO deck (native_language, learning_language, " +
                        "total_words, words_to_learn, known_words, learned_words,user_id )" +
                        " VALUES (?, ?, ?, ?, ?, ?, ? )";

        jdbcTemplate.update(sql,deck.getNativeLanguage().name(),deck.getLearningLanguage().name(),0, 0, 0, 0,user.getId());
    }

    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM deck WHERE id = ?";
        jdbcTemplate.update(sql,id);
    }


}
