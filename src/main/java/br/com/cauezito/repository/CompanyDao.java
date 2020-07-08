package br.com.cauezito.repository;

import br.com.cauezito.entity.Company;

public interface CompanyDao {
	Company findCompany(String email, String password, String cnpj);
}
