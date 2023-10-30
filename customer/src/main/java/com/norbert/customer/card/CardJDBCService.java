package com.norbert.customer.card;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CardJDBCService implements CardDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Long deckId,CardDTO card) {
        String insertCardSql = "INSERT INTO card (native_word, translation, description, state, deck_id) VALUES (?, ?, ?, ?, ?)";
        String updateDeckSql = "UPDATE deck SET total_words = total_words + 1, words_to_learn = words_to_learn + 1 WHERE id = ?";

        jdbcTemplate.update(insertCardSql, card.getNativeWord(), card.getTranslation(), card.getDescription(), State.TO_LEARN.name(), deckId);
        jdbcTemplate.update(updateDeckSql, deckId);
    }


    @Override
    public List<Card> getCardsByDeckId(Long deckId) {
        String sql =
                "SELECT * " +
                "FROM card  " +
                "WHERE deck_id = ?";

        return extractCards(deckId, sql);

    }

    @Override
    public void removeCard(Long id) {
        String sql = "DELETE FROM card WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Card> findCardById(Long id) {
        String sql = "SELECT * FROM card WHERE id = ?";
        List<Card> cards = extractCards(id,sql);

        if (!cards.isEmpty()) {
            return Optional.of(cards.get(0));
        } else {
            return Optional.empty();
        }
    }

    private List<Card> extractCards(Long id, String sql) {
        List<Card> cards = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            Card card = new Card();
            card.setId(rs.getLong("id"));
            card.setNativeWord(rs.getString("native_word"));
            card.setTranslation(rs.getString("translation"));
            card.setDescription(rs.getString("description"));
            card.setState(State.valueOf(rs.getString("state")));

            return card;
        });
        return cards;
    }


}
