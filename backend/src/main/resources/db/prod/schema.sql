CREATE TABLE IF NOT EXISTS members (
  id BIGINT AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  display_name VARCHAR(255) NOT NULL,
  profile_image_url VARCHAR(255) NOT NULL,
  social_type VARCHAR(255) NOT NULL,
  created_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  members_id BIGINT NOT NULL,
  category_type VARCHAR(255) NOT NULL,
  created_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (members_id) REFERENCES members (id)
);

CREATE TABLE IF NOT EXISTS subscriptions (
  id BIGINT AUTO_INCREMENT,
  color VARCHAR(255) NOT NULL,
  checked boolean NOT NULL,
  members_id BIGINT NOT NULL,
  categories_id BIGINT NOT NULL,
  created_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (members_id) REFERENCES members (id),
  FOREIGN KEY (categories_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS schedules (
  id BIGINT AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  start_date_time datetime NOT NULL,
  end_date_time datetime NOT NULL,
  memo VARCHAR(255) NOT NULL,
  categories_id BIGINT NOT NULL,
  created_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (categories_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS oauth_tokens (
  id BIGINT AUTO_INCREMENT,
  refresh_token VARCHAR(255) NOT NULL,
  members_id BIGINT NOT NULL,
  created_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (members_id) REFERENCES members (id)
);

CREATE TABLE IF NOT EXISTS external_category_details (
    id BIGINT AUTO_INCREMENT,
    categories_id BIGINT NOT NULL,
    external_id VARCHAR(255) NOT NULL,
    created_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (categories_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS category_roles (
    id BIGINT AUTO_INCREMENT,
    members_id BIGINT NOT NULL,
    categories_id BIGINT NOT NULL,
    category_role_type VARCHAR(255),
    created_at datetime(6) not null DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime(6) not null DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (categories_id) REFERENCES categories (id),
    FOREIGN KEY (members_id) REFERENCES members (id)
);
