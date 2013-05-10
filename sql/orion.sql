CREATE TABLE IF NOT EXISTS `aliases` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `client_id` int(10) unsigned NOT NULL,
  `name` varchar(64) NOT NULL,
  `num_used` int(10) unsigned NOT NULL DEFAULT '1',
  `time_add` bigint(20) unsigned NOT NULL,
  `time_edit` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `client_id` (`client_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `callvotes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `client_id` int(10) unsigned NOT NULL,
  `type` varchar(32) NOT NULL,
  `data` varchar(64) DEFAULT NULL,
  `yes` smallint(2) unsigned NOT NULL,
  `no` smallint(2) unsigned NOT NULL,
  `time_add` bigint(20) unsigned NOT NULL,
  `time_edit` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `clients` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` smallint(3) unsigned NOT NULL,
  `name` varchar(64) NOT NULL,
  `connections` int(10) unsigned NOT NULL DEFAULT '0',
  `ip` varchar(15) NOT NULL,
  `guid` varchar(32) NOT NULL,
  `auth` varchar(64) DEFAULT NULL,
  `time_add` bigint(20) unsigned NOT NULL,
  `time_edit` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `groups` (
  `id` smallint(3) unsigned NOT NULL,
  `name` varchar(20) NOT NULL,
  `keyword` varchar(20) NOT NULL,
  `level` smallint(3) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `groups` (`id`, `name`, `keyword`, `level`) VALUES
(0, 'Guest', 'guest', 0),
(1, 'User', 'user', 1),
(2, 'Regular', 'regular', 2),
(4, 'Friend', 'friend', 10),
(8, 'Moderator', 'moderator', 20),
(16, 'Admin', 'admin', 40),
(32, 'Full Admin', 'fulladmin', 60),
(64, 'Senior Admin', 'senioradmin', 80),
(128, 'Super Admin', 'superadmin', 100);

CREATE TABLE IF NOT EXISTS `ipaliases` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `client_id` int(10) unsigned NOT NULL,
  `ip` varchar(15) NOT NULL,
  `num_used` int(10) unsigned NOT NULL DEFAULT '1',
  `time_add` bigint(20) unsigned NOT NULL,
  `time_edit` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `client_id` (`client_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `penalties` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `client_id` int(10) unsigned NOT NULL,
  `admin_id` int(10) unsigned DEFAULT NULL,
  `type` enum('BAN','KICK','WARNING') NOT NULL,
  `active` bit(1) NOT NULL DEFAULT b'1',
  `reason` varchar(256) NOT NULL,
  `time_add` bigint(20) unsigned NOT NULL,
  `time_edit` bigint(20) unsigned NOT NULL,
  `time_expire` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `client_id` (`client_id`,`admin_id`),
  KEY `admin_id` (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


ALTER TABLE `aliases`
  ADD CONSTRAINT `aliases_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `callvotes`
  ADD CONSTRAINT `callvotes_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `clients`
  ADD CONSTRAINT `clients_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON UPDATE CASCADE;

ALTER TABLE `ipaliases`
  ADD CONSTRAINT `ipaliases_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `penalties`
  ADD CONSTRAINT `penalties_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `penalties_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `clients` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;
  