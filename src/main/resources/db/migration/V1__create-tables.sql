CREATE TABLE IF NOT EXISTS candidato (
    id SERIAL PRIMARY KEY,
    CPF VARCHAR NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    CEP VARCHAR(9) NOT NULL,
    description TEXT,
    password VARCHAR(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS empresa (
    id SERIAL PRIMARY KEY,
    empresa_name VARCHAR(100) NOT NULL,
    description TEXT,
    email VARCHAR(100) NOT NULL,
    cnpj VARCHAR(18) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    country VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS vaga (
    id SERIAL PRIMARY KEY,
    vaga_name VARCHAR(100) NOT NULL,
    description TEXT,
    empresa_id INT NOT NULL,
    state VARCHAR(2) NOT NULL,
    city VARCHAR(100) NOT NULL,
    CONSTRAINT fk_empresa
        FOREIGN KEY (empresa_id)
        REFERENCES empresa(id)
);

CREATE TABLE IF NOT EXISTS competencia_by_enum (
    id SERIAL PRIMARY KEY,
    description VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS competencia_input (
    id SERIAL PRIMARY KEY,
    description VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS candidato_competencia (
    id SERIAL PRIMARY KEY,
    candidato_id INT NOT NULL,
    competencia_id INT NOT NULL,
    FOREIGN KEY (candidato_id) REFERENCES candidato(id) ON DELETE CASCADE,
    FOREIGN KEY (competencia_id) REFERENCES competencia_by_enum(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vaga_competencia (
    id SERIAL PRIMARY KEY,
    vaga_id INT NOT NULL,
    competencia_id INT NOT NULL,
    FOREIGN KEY (vaga_id) REFERENCES vaga(id) ON DELETE CASCADE,
    FOREIGN KEY (competencia_id) REFERENCES competencia_by_enum(id) ON DELETE CASCADE
);

INSERT INTO competencia_by_enum (description) VALUES
('JavaScript'),
('TypeScript'),
('Python'),
('Java'),
('C#'),
('PHP'),
('Kotlin'),
('Swift'),
('SQL'),
('NoSQL'),
('HTML'),
('CSS'),
('React'),
('Angular'),
('Vue'),
('Node.js'),
('Spring Boot'),
('Django'),
('Docker'),
('Kubernetes'),
('AWS'),
('Azure'),
('Git'),
('DevOps'),
('Test-Driven Development'),
('CI/CD');