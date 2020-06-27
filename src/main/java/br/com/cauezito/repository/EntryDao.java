package br.com.cauezito.repository;

import java.util.List;

import br.com.cauezito.entity.Entry;

public interface EntryDao {
	List<Entry> list(Long userId);
}
