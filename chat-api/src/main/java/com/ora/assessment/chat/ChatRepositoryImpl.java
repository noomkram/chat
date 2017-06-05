package com.ora.assessment.chat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.ora.assessment.chat.message.Message;
import com.ora.assessment.user.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatRepositoryImpl implements CustomChatRepository {

  // @formatter:off
  public static final String GET_CHATS_SQL = "SELECT c.*, m1.*, u.name AS user_name, u.email FROM chats c "
      + "JOIN messages m1 ON (c.chat_id = m1.chat_id) "
      + "LEFT OUTER JOIN messages m2 ON (c.chat_id = m2.chat_id AND m1.created > m2.created) "
      + "JOIN chat_users u ON m1.user_id = u.user_id "
      + "WHERE m2.created IS null offset ? limit ?";

  public static final String GET_CHAT_USERS_SQL = "SELECT DISTINCT m.chat_id, u.email, u.user_id, u.name as user_name FROM chat_users u "
      + "JOIN messages m ON u.user_id = m.user_id "
      + "WHERE m.chat_id in (:ids)";
  // @formatter:on

  @Autowired
  private NamedParameterJdbcTemplate template;
  private final ChatResultSetExtractor chatExtractor;
  private final UserRowMapper userMapper;

  public ChatRepositoryImpl() {
    this.chatExtractor = new ChatResultSetExtractor();
    this.userMapper = new UserRowMapper();
  }

  @Override
  public List<Chat> find(Pageable pageable) {
    int offset = (pageable.getPageNumber()-1)*pageable.getPageSize(); // need to adjust for PageableHandlerMethodArgumentResolver.setOneIndexedParameters
    log.debug("offset [{}]", offset);

    Map<Long, Chat> chats = template.getJdbcOperations().query(GET_CHATS_SQL, chatExtractor, offset, pageable.getPageSize());
    log.debug("found [{}] chats", chats.size());

    if (!chats.isEmpty()) {
      MapSqlParameterSource parameters = new MapSqlParameterSource();
      parameters.addValue("ids", chats.values().stream().map(Chat::getId).collect(Collectors.toList()));

      template.query(GET_CHAT_USERS_SQL, parameters, new RowCallbackHandler() {

        @Override
        public void processRow(ResultSet rs) throws SQLException {
          chats.get(rs.getLong("chat_id")).getUsers().add(userMapper.mapRow(rs, rs.getRow()));
        }

      });
    }

    return new ArrayList<Chat>(chats.values());
  }

  private class ChatResultSetExtractor implements ResultSetExtractor<Map<Long, Chat>> {

    @Override
    public Map<Long, Chat> extractData(ResultSet rs) throws SQLException, DataAccessException {
      Map<Long, Chat> data = new LinkedHashMap<>();
      while (rs.next()) {
        final User u = userMapper.mapRow(rs, rs.getRow());

        final Message m = new Message();
        m.setChatId(rs.getLong("chat_id"));
        m.setCreated(rs.getDate("created"));
        m.setId(rs.getLong("message_id"));
        m.setMessage(rs.getString("message"));
        m.setUser(u);

        final Chat c = new Chat();
        c.setId(m.getChatId());
        c.setName(rs.getString("name"));
        c.setMessage(m);

        data.put(c.getId(), c);
      }
      return data;
    }
  }

  private class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      final User u = new User();
      u.setEmail(rs.getString("email"));
      u.setId(rs.getLong("user_id"));
      u.setName(rs.getString("user_name"));
      return u;
    }

  }

}
