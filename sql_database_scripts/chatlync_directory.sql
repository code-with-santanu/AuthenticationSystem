CREATE DATABASE  IF NOT EXISTS `chatlync_directory`;
USE `chatlync_directory`;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `token`;
DROP TABLE IF EXISTS `users`;


CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum ('USER','ADMIN'),
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


--
-- Table structure for table `token`
--



create table `token` (
	`id` bigint not null auto_increment,
	`logged_out` bit DEFAULT NULL,
    `logged_out_time` datetime DEFAULT NULL,
    `token` varchar(255),
    `user_id` bigint,
    primary key (id)) engine=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

alter table `token` add constraint fk_user foreign key (`user_id`) references `users` (id);

-- INSERT INTO `users` VALUES 
-- 	(1,'Leslie','Andrews','leslie@luv2code.com','12345','123'),
-- 	(2,'Emma','Baumgarten','emma@luv2code.com','12345','123'),
-- 	(3,'Avani','Gupta','avani@luv2code.com','12345','123'),
-- 	(4,'Yuri','Petrov','yuri@luv2code.com','12345','123'),
-- 	(5,'Juan','Vega','juan@luv2code.com','12345','123');