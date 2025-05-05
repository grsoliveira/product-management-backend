-- product_management.category definition

CREATE TABLE `category` (
                            `tax_id` binary(16) NOT NULL,
                            `name` varchar(255) NOT NULL,
                            `parent_id` binary(16) DEFAULT NULL,
                            PRIMARY KEY (`tax_id`),
                            KEY `FK2y94svpmqttx80mshyny85wqr` (`parent_id`),
                            CONSTRAINT `FK2y94svpmqttx80mshyny85wqr` FOREIGN KEY (`parent_id`) REFERENCES `category` (`tax_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- product_management.product definition

CREATE TABLE `product` (
                           `id` binary(16) NOT NULL,
                           `name` varchar(255) NOT NULL,
                           `price` decimal(10,2) NOT NULL,
                           `category_id` binary(16) NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FK1mtsbur82frn64de7balymq9s` (`category_id`),
                           CONSTRAINT `FK1mtsbur82frn64de7balymq9s` FOREIGN KEY (`category_id`) REFERENCES `category` (`tax_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;