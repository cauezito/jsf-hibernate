package br.com.cauezito.repository;

import java.util.List;

import br.com.cauezito.entity.Message;

public interface MessageDao {
	List<Message> allMessages(Long userId);
}
