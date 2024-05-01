CREATE DATABASE  IF NOT EXISTS `chatlync_directory`;
USE `chatlync_directory`;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum ('USER','ADMIN'),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



-- INSERT INTO `users` VALUES 
-- 	(1,'Leslie','Andrews','leslie@luv2code.com','12345','123'),
-- 	(2,'Emma','Baumgarten','emma@luv2code.com','12345','123'),
-- 	(3,'Avani','Gupta','avani@luv2code.com','12345','123'),
-- 	(4,'Yuri','Petrov','yuri@luv2code.com','12345','123'),
-- 	(5,'Juan','Vega','juan@luv2code.com','12345','123');