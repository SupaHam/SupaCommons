package com.supaham.commons.jdbc.spring;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import javax.annotation.Nonnull;

/**
 * Represents a simple batch setter abstract implementation that provides convenience by taking
 * away the requirement of having to implement {@link #getBatchSize()}. This class allows for
 * lambda usage with Java 1.8.
 * <p />
 * Example usage:
 * <pre>
 * new SimpleBatchSetter&lt;&gt;(answers, (ps, answer) -> {
 *     ps.setString(1, answer.getText());
 *     ps.setInt(2, answer.getAnswerId());
 * }
 * </pre>
 *
 * Compared to:
 * <pre>
 * new BatchPreparedStatementSetter() {
 *    {@literal @}Override public void setValues(PreparedStatement ps, int i) throws SQLException {
 *         final AnswerEntry answerEntry = answers.get(i);
 *         ps.setString(1, answerEntry.getText());
 *         ps.setInt(2, answerEntry.getPollId());
 *     }
 *     
 *    {@literal @}Override public int getBatchSize() {
 *         return answers.size();
 *     }
 * }</pre>
 */
public class SimpleBatchSetter<T> implements BatchPreparedStatementSetter {

  private final Collection<T> collection;
  private final BatchConsumer<T> consumer;

  public SimpleBatchSetter(Collection<T> collection, @Nonnull BatchConsumer<T> consumer) {
    this.collection = collection;
    this.consumer = Preconditions.checkNotNull(consumer, "consumer cannot be null.");
  }

  @Override public void setValues(PreparedStatement ps, int i) throws SQLException {
    consumer.consume(ps, Iterables.get(this.collection, i));
  }

  @Override public int getBatchSize() {
    return this.collection.size();
  }

  public interface BatchConsumer<T> {

    /**
     * @see BatchPreparedStatementSetter#setValues(PreparedStatement, int)
     */
    void consume(PreparedStatement stmt, T element) throws SQLException;
  }
}
