CREATE DATABASE IF NOT EXISTS truthtables;

use truthtables;

CREATE TABLE `QUESTIONS` (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `question` varchar(200) NOT NULL,
                             `chapter` int(10) NOT NULL,
                             `chars` varchar(10) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2714 DEFAULT CHARSET=utf8;