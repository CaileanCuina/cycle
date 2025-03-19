CREATE TABLE users (
                       user_name VARCHAR(50) PRIMARY KEY,
                       cycle_std float,
                       avg_cycle_len float,
                       avg_mens_len float
);

CREATE TABLE cycle_information (
                                   id BIGINT PRIMARY KEY,
                                   start_date date,
                                   end_date date,
                                   user_name VARCHAR(50),
                                   FOREIGN KEY (user_name) REFERENCES users(user_name) ON DELETE CASCADE
);
CREATE SEQUENCE CYCLE_INFORMATION_SEQ START WITH 100 INCREMENT BY 50;
CREATE TABLE cycle_event (
                                    id BIGINT PRIMARY KEY,
                                    occurrence_date date,
                                    type varchar(12),
                                    cycle_information_id BIGINT,
                                    FOREIGN KEY (cycle_information_id) REFERENCES cycle_information(id) ON DELETE CASCADE
);
CREATE SEQUENCE cycle_event_SEQ START WITH 100 INCREMENT BY 50;