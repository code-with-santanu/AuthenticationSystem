CREATE DATABASE  IF NOT EXISTS `auth_directory`;
USE `auth_directory`;

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
    primary key (id),
    constraint fk_user foreign key (`user_id`) references `users` (id) ON DELETE CASCADE
    ) engine=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
