package org.springframework.samples.nt4h.message;

import lombok.AllArgsConstructor;
import org.springframework.samples.nt4h.game.Game;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public void saveMessage(Message message) {
        message.setTime(LocalDateTime.now());
        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<Message> getMessageByPair(String user1, String user2) {
        return messageRepository.findByPair(user1, user2);
    }

    @Transactional(readOnly = true)
    public List<Message> getMessageBySenderWithReceiver(String usernameSender, String usernameReceiver) {
        return messageRepository.findBySenderWithReceiver(usernameSender, usernameReceiver);
    }

    @Transactional(readOnly = true)
    public Integer getUnreadMessages(String usernameSender, String usernameReceiver) {
        return getMessageBySenderWithReceiver(usernameSender, usernameReceiver).stream().filter(message -> !message.isRead()).toArray().length;
    }

    @Transactional
    public void deleteMessage(Message message) {
        message.onDeleteSetNull();
        messageRepository.save(message);
        messageRepository.delete(message);
    }

    @Transactional
    public Object getGameMessages(Game game) {
        return messageRepository.findByGame(game);
    }

    @Transactional
    public void markAsRead(String usernameSender, String usernameReceiver) {
        List<Message> messages = getMessageBySenderWithReceiver(usernameSender, usernameReceiver);
        messages.stream().filter(message -> !message.isRead()).forEach(message -> message.setRead(true));
        messageRepository.saveAll(messages);
    }

    @Transactional
    public void createNotification(Game game, String content) {
        Message notification = new Message();
        notification.setGame(game);
        notification.setContent(content);
        notification.setType(MessageType.ADVISE);
        saveMessage(notification);
    }
}
