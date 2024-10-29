CREATE TABLE IF NOT EXISTS icd(
    code VARCHAR(8) PRIMARY KEY,
    name VARCHAR(512) NOT NULL
);

CREATE TABLE IF NOT EXISTS patients (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    name varchar(64) NOT NULL,
    surname varchar(128) NOT NULL,
    patronymic varchar(128) DEFAULT '' NOT NULL,
    gender CHAR(1) NOT NULL,
    birth_date DATE NOT NULL,
    policy_number CHAR(16) UNIQUE NOT NULL,

    CONSTRAINT policy_number_check CHECK (policy_number ~ '^[0-9]*$'),
    CONSTRAINT birth_date_check CHECK (birth_date <= CURRENT_DATE)
);

CREATE TABLE IF NOT EXISTS disease (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    starting DATE NOT NULL,
    ending DATE DEFAULT NULL,
    icd_code VARCHAR(8) NOT NULL,
    patient_id bigint NOT NULL,
    description VARCHAR(2048) NOT NULL,

    CONSTRAINT fk_icd_code FOREIGN KEY (icd_code) REFERENCES icd (code),
    CONSTRAINT fk_patient_id FOREIGN KEY (patient_id) REFERENCES patients (id),
    CONSTRAINT date_check CHECK (starting <= CURRENT_DATE AND ending <= CURRENT_DATE)
);



