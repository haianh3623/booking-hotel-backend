-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: hotel-management
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
                           `address_id` int NOT NULL AUTO_INCREMENT,
                           `city` varchar(255) DEFAULT NULL,
                           `district` varchar(255) DEFAULT NULL,
                           `specific_address` varchar(255) DEFAULT NULL,
                           `ward` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`address_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'Hanoi','Hoan Kiem','123 Phan Chu Trinh','Cua Nam'),(2,'Hanoi','Ba Dinh','56 Hang Bong','Lien Quan'),(3,'Ho Chi Minh City','District 1','99 Nguyen Hue','Ben Nghe'),(4,'Da Nang','Son Tra','88 Vo Nguyen Giap','My Khe');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill` (
                        `bill_id` int NOT NULL AUTO_INCREMENT,
                        `created_at` datetime(6) DEFAULT NULL,
                        `updated_at` datetime(6) DEFAULT NULL,
                        `paid_status` bit(1) DEFAULT NULL,
                        `total_price` double DEFAULT NULL,
                        `user_id` int NOT NULL,
                        PRIMARY KEY (`bill_id`),
                        KEY `FKqhq5aolak9ku5x5mx11cpjad9` (`user_id`),
                        CONSTRAINT `FKqhq5aolak9ku5x5mx11cpjad9` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES (1,NULL,NULL,_binary '',450,1),(2,NULL,NULL,_binary '\0',320,2),(3,NULL,NULL,_binary '',600,3),(4,NULL,NULL,_binary '',200,4),(5,'2025-04-16 01:06:42.542410',NULL,_binary '\0',1500000,1),(6,'2025-04-16 07:52:31.409128',NULL,_binary '\0',5000,1),(7,'2025-04-16 07:54:24.688635',NULL,_binary '\0',5000,1),(8,'2025-04-16 08:05:06.000185',NULL,_binary '\0',5000,1),(9,'2025-04-16 08:12:03.410195',NULL,_binary '\0',5000,1),(10,'2025-04-16 08:15:00.331498','2025-04-16 08:15:14.740059',_binary '',5000,1),(11,'2025-04-16 08:20:35.483040',NULL,_binary '\0',80,1),(12,'2025-04-16 08:21:14.640206',NULL,_binary '\0',5000,1),(13,'2025-04-16 08:34:06.959289',NULL,_binary '\0',5000,1),(14,'2025-04-16 08:39:16.834246',NULL,_binary '\0',5000,1),(15,'2025-04-16 08:40:28.636861',NULL,_binary '\0',5000,1),(16,'2025-04-16 09:51:01.992782',NULL,_binary '\0',4000,1),(17,'2025-04-16 10:02:20.743930','2025-04-16 10:02:41.996589',_binary '',5000,1),(18,'2025-04-16 10:09:17.666260','2025-04-16 10:09:37.401953',_binary '',5000,1),(19,'2025-04-16 10:14:05.359708','2025-04-16 10:14:28.405579',_binary '',5000,1),(20,'2025-04-16 10:58:35.113852',NULL,_binary '\0',5000,1),(21,'2025-04-16 11:00:45.101106','2025-04-16 11:00:47.440216',_binary '',5000,1),(22,'2025-04-16 11:04:50.254638','2025-04-16 11:04:52.508104',_binary '',5000,1),(23,'2025-04-16 11:05:43.722661','2025-04-16 11:05:45.280619',_binary '',5000,1),(24,'2025-04-16 11:07:20.391803','2025-04-16 11:07:23.811130',_binary '',5000,1),(25,'2025-04-16 11:14:47.762995','2025-04-16 11:14:49.132684',_binary '',5000,1);
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
                           `booking_id` int NOT NULL AUTO_INCREMENT,
                           `created_at` datetime(6) DEFAULT NULL,
                           `updated_at` datetime(6) DEFAULT NULL,
                           `check_in` datetime(6) DEFAULT NULL,
                           `check_out` datetime(6) DEFAULT NULL,
                           `bill_id` int DEFAULT NULL,
                           `room_id` int NOT NULL,
                           `user_id` int NOT NULL,
                           `status` varchar(255) DEFAULT NULL,
                           `price` double DEFAULT NULL,
                           PRIMARY KEY (`booking_id`),
                           KEY `FKgi35fflyy8jm388q42yn93hu8` (`bill_id`),
                           KEY `FKq83pan5xy2a6rn0qsl9bckqai` (`room_id`),
                           KEY `FKkgseyy7t56x7lkjgu3wah5s3t` (`user_id`),
                           CONSTRAINT `FKgi35fflyy8jm388q42yn93hu8` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`bill_id`),
                           CONSTRAINT `FKkgseyy7t56x7lkjgu3wah5s3t` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
                           CONSTRAINT `FKq83pan5xy2a6rn0qsl9bckqai` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES (1,'2025-04-13 01:17:00.598933',NULL,'2025-05-01 14:00:00.000000','2025-05-05 12:00:00.000000',1,1,1,'CONFIRMED',1000),(2,'2025-04-13 01:17:00.598933',NULL,'2025-06-01 15:00:00.000000','2025-06-03 11:00:00.000000',2,2,2,'CONFIRMED',21000),(3,'2025-04-13 01:17:00.598933',NULL,'2025-07-01 16:00:00.000000','2025-07-05 13:00:00.000000',3,3,3,'CONFIRMED',1000),(4,'2025-04-13 01:17:00.598933',NULL,'2025-08-01 18:00:00.000000','2025-08-02 11:00:00.000000',4,4,4,'CONFIRMED',1000),(5,'2025-04-13 01:17:00.598933',NULL,'2025-05-10 14:00:00.000000','2025-05-12 12:00:00.000000',1,1,1,'CANCELLED',1000),(6,'2025-04-13 01:17:00.598933',NULL,'2025-06-15 15:00:00.000000','2025-06-17 11:00:00.000000',1,1,1,'CANCELLED',21000),(7,'2025-04-13 01:17:00.598933',NULL,'2025-07-10 16:00:00.000000','2025-07-12 13:00:00.000000',3,1,1,'CANCELLED',31000),(8,'2025-04-13 01:17:00.598933',NULL,'2025-08-15 18:00:00.000000','2025-08-17 11:00:00.000000',1,1,1,'PENDING',51000),(9,'2025-04-13 01:17:00.598933',NULL,'2025-09-01 14:00:00.000000','2025-09-03 12:00:00.000000',2,1,1,'PENDING',2110),(21,'2025-04-13 01:17:00.598933',NULL,'2025-04-13 14:00:00.000000','2025-04-14 11:00:00.000000',NULL,9,1,'CONFIRMED',1000),(22,'2025-04-13 01:18:18.686107',NULL,'2025-04-13 14:00:00.000000','2025-04-14 11:00:00.000000',NULL,6,1,'CONFIRMED',5000),(23,'2025-04-13 01:19:34.075891',NULL,'2025-04-13 14:00:00.000000','2025-04-14 11:00:00.000000',NULL,6,1,'PENDING',5000),(24,'2025-04-13 01:20:56.742749',NULL,'2025-04-13 14:00:00.000000','2025-04-14 11:00:00.000000',NULL,6,1,'PENDING',5000),(25,'2025-04-13 01:21:57.072664',NULL,'2025-04-13 14:00:00.000000','2025-04-14 11:00:00.000000',NULL,6,1,'PENDING',5000),(26,'2025-04-13 01:25:10.049062',NULL,'2025-04-13 14:00:00.000000','2025-04-14 11:00:00.000000',NULL,6,1,'PENDING',5000),(27,'2025-04-13 01:27:42.918428',NULL,'2025-04-13 14:00:00.000000','2025-04-14 11:00:00.000000',NULL,6,1,'PENDING',5000),(28,'2025-04-13 01:30:57.204183',NULL,'2025-04-13 14:00:00.000000','2025-04-14 11:00:00.000000',NULL,6,1,'PENDING',5000),(29,'2025-04-13 01:32:39.968232',NULL,'2025-04-13 14:00:00.000000','2025-04-14 11:00:00.000000',NULL,6,1,'PENDING',5000),(30,'2025-04-13 01:35:26.592465',NULL,'2025-04-13 14:00:00.000000','2025-04-14 11:00:00.000000',NULL,6,1,'PENDING',5000),(31,'2025-04-13 01:37:56.987602',NULL,'2025-04-13 14:00:00.000000','2025-04-14 11:00:00.000000',NULL,6,1,'PENDING',5000),(32,'2025-04-16 00:46:48.055995',NULL,'2025-04-16 14:00:00.000000','2025-04-17 11:00:00.000000',NULL,6,1,'PENDING',4000),(33,'2025-04-16 00:46:56.843642',NULL,'2025-04-16 14:00:00.000000','2025-04-17 11:00:00.000000',NULL,6,1,'PENDING',4000),(34,'2025-04-16 00:50:02.123155',NULL,'2025-04-16 14:00:00.000000','2025-04-17 11:00:00.000000',NULL,6,1,'PENDING',4000),(35,'2025-04-16 00:52:29.376136',NULL,'2025-04-16 14:00:00.000000','2025-04-17 11:00:00.000000',NULL,6,1,'PENDING',4000),(36,'2025-04-16 00:53:45.269280',NULL,'2025-04-16 14:00:00.000000','2025-04-17 11:00:00.000000',NULL,6,1,'PENDING',4000),(37,'2025-04-16 01:03:23.358386',NULL,'2025-04-16 14:00:00.000000','2025-04-17 11:00:00.000000',NULL,6,1,'PENDING',4000),(38,'2025-04-16 01:04:35.794294',NULL,'2025-04-16 14:00:00.000000','2025-04-18 12:00:00.000000',NULL,2,1,'PENDING',1500000),(39,'2025-04-16 01:06:42.625723',NULL,'2025-04-16 14:00:00.000000','2025-04-18 12:00:00.000000',5,2,1,'PENDING',1500000),(41,'2025-04-16 07:54:24.703335',NULL,'2025-04-16 14:00:00.000000','2025-04-17 11:00:00.000000',7,7,1,'PENDING',5000),(42,'2025-04-16 08:05:06.014186','2025-04-16 08:05:36.098077','2025-04-16 14:00:00.000000','2025-04-17 11:00:00.000000',8,7,1,'CONFIRMED',5000),(43,'2025-04-16 08:12:03.505434',NULL,'2025-04-16 14:00:00.000000','2025-04-17 11:00:00.000000',9,8,1,'PENDING',5000),(44,'2025-04-16 08:15:00.406892','2025-04-16 08:15:14.794954','2025-04-16 14:00:00.000000','2025-04-17 11:00:00.000000',10,9,1,'CONFIRMED',5000),(45,'2025-04-16 08:20:35.499035',NULL,'2025-04-16 14:00:00.000000','2025-04-17 11:00:00.000000',11,4,1,'PENDING',80),(48,'2025-04-16 08:39:16.843986',NULL,'2025-04-18 14:00:00.000000','2025-04-19 11:00:00.000000',14,6,1,'PENDING',5000),(51,'2025-04-16 10:02:20.756849','2025-04-16 10:02:42.057601','2025-04-19 14:00:00.000000','2025-04-20 11:00:00.000000',17,9,1,'CONFIRMED',5000),(52,'2025-04-16 10:09:17.731929','2025-04-16 10:09:37.449095','2025-04-18 14:00:00.000000','2025-04-19 11:00:00.000000',18,9,1,'CONFIRMED',5000),(53,'2025-04-16 10:14:05.371924','2025-04-16 10:14:28.439792','2025-04-18 14:00:00.000000','2025-04-19 11:00:00.000000',19,7,1,'CONFIRMED',5000),(56,'2025-04-16 11:04:50.266631','2025-04-16 11:04:52.603102','2025-04-25 14:00:00.000000','2025-04-26 11:00:00.000000',22,6,1,'CONFIRMED',5000);
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hotel`
--

DROP TABLE IF EXISTS `hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotel` (
                         `hotel_id` int NOT NULL AUTO_INCREMENT,
                         `created_at` datetime(6) DEFAULT NULL,
                         `updated_at` datetime(6) DEFAULT NULL,
                         `address` varchar(255) DEFAULT NULL,
                         `hotel_name` varchar(255) DEFAULT NULL,
                         `user_id` int DEFAULT NULL,
                         `address_id` int DEFAULT NULL,
                         PRIMARY KEY (`hotel_id`),
                         UNIQUE KEY `UKowt8iiq4d3dff6uvmdyjbmmar` (`address_id`),
                         KEY `FKi0j3nnn6eecin1ry6cujioqq2` (`user_id`),
                         CONSTRAINT `FK48m0ei7s6biikxbrku04slp0s` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`),
                         CONSTRAINT `FKi0j3nnn6eecin1ry6cujioqq2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotel`
--

LOCK TABLES `hotel` WRITE;
/*!40000 ALTER TABLE `hotel` DISABLE KEYS */;
INSERT INTO `hotel` VALUES (1,NULL,NULL,NULL,'Grand Hotel Hanoi',1,1),(2,NULL,NULL,NULL,'Royal Palace',2,2),(3,NULL,NULL,NULL,'Sunshine Hotel',3,3),(4,NULL,NULL,NULL,'Beachside Resort',4,4);
/*!40000 ALTER TABLE `hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
                                `noti_id` int NOT NULL AUTO_INCREMENT,
                                `created_at` datetime(6) DEFAULT NULL,
                                `updated_at` datetime(6) DEFAULT NULL,
                                `content` varchar(255) DEFAULT NULL,
                                `user_id` int DEFAULT NULL,
                                PRIMARY KEY (`noti_id`),
                                KEY `FKb0yvoep4h4k92ipon31wmdf7e` (`user_id`),
                                CONSTRAINT `FKb0yvoep4h4k92ipon31wmdf7e` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
                          `review_id` int NOT NULL AUTO_INCREMENT,
                          `created_at` datetime(6) DEFAULT NULL,
                          `updated_at` datetime(6) DEFAULT NULL,
                          `content` varchar(255) DEFAULT NULL,
                          `rating` int DEFAULT NULL,
                          `booking_id` int NOT NULL,
                          PRIMARY KEY (`review_id`),
                          KEY `FKk4xawqohtguy5yx5nnpba6yf3` (`booking_id`),
                          CONSTRAINT `FKk4xawqohtguy5yx5nnpba6yf3` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,'2025-04-14 17:03:52.386194','2025-04-14 17:11:45.460110','The service was excellent, I loved my stay!',3,1),(3,'2025-04-15 01:00:05.550135',NULL,'OK',5,1),(4,'2025-04-15 01:01:18.901244',NULL,'Tam',3,1),(5,'2025-04-15 01:11:41.307705',NULL,'Tuyet',3,1),(6,'2025-04-15 23:53:49.903503',NULL,'Ok lam',5,21);
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
                        `role_id` int NOT NULL AUTO_INCREMENT,
                        `created_at` datetime(6) DEFAULT NULL,
                        `updated_at` datetime(6) DEFAULT NULL,
                        `name` varchar(255) DEFAULT NULL,
                        `time_created` datetime(6) DEFAULT NULL,
                        PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,NULL,NULL,'ROLE_CUSTOMER',NULL),(2,NULL,NULL,'ROLE_ADMIN',NULL),(3,NULL,NULL,'ROLE_HOTEL_OWNER',NULL);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
                        `room_id` int NOT NULL AUTO_INCREMENT,
                        `created_at` datetime(6) DEFAULT NULL,
                        `updated_at` datetime(6) DEFAULT NULL,
                        `area` double DEFAULT NULL,
                        `bed_number` int DEFAULT NULL,
                        `combo_price2h` double DEFAULT NULL,
                        `description` varchar(255) DEFAULT NULL,
                        `extra_adult` double DEFAULT NULL,
                        `extra_hour_price` double DEFAULT NULL,
                        `max_occupancy` int DEFAULT NULL,
                        `num_children_free` int DEFAULT NULL,
                        `price_per_night` double DEFAULT NULL,
                        `room_img` varchar(255) DEFAULT NULL,
                        `room_name` varchar(255) DEFAULT NULL,
                        `standard_occupancy` int DEFAULT NULL,
                        `hotel_id` int DEFAULT NULL,
                        PRIMARY KEY (`room_id`),
                        KEY `FKdosq3ww4h9m2osim6o0lugng8` (`hotel_id`),
                        CONSTRAINT `FKdosq3ww4h9m2osim6o0lugng8` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,NULL,NULL,30,2,100,'Spacious room with a great view',20,30,4,1,150,'3.png','Deluxe Room',2,1),(2,NULL,NULL,25,2,80,'Comfortable room with basic amenities',15,25,3,1,100,'3.png','Standard Room',2,1),(3,NULL,NULL,40,2,150,'Luxurious suite with premium services',30,50,5,2,250,'superior_suite.jpg','Superior Suite',2,2),(4,NULL,NULL,20,1,50,'Compact room with essential facilities',10,20,2,0,70,'economy_room.jpg','Economy Room',1,3),(5,NULL,NULL,35,2,120,'Room with a stunning ocean view',25,40,4,1,180,'ocean_view_room.jpg','Ocean View Room',2,4),(6,'2025-04-09 09:26:40.662848',NULL,30,1,200000,'Nice room',1,50000,3,1,5000,'3.png','Deluxe Room',2,1),(7,'2025-04-09 10:05:23.490400',NULL,30,1,200000,'Nice room',1,50000,3,1,5000,'8.png','Deluxe Room3',2,1),(8,'2025-04-09 10:33:17.050208',NULL,30,1,200000,'Nice room',1,50000,3,1,5000,'CHIBI FUNKY 1-min.png','Deluxe Room3',2,1),(9,'2025-04-09 10:41:09.904129',NULL,30,1,200000,'Nice room',1,50000,3,1,5000,'CHIBI FUNKY 2-min.png','Deluxe Room3',2,1);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_image`
--

DROP TABLE IF EXISTS `room_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_image` (
                              `img_id` int NOT NULL AUTO_INCREMENT,
                              `created_at` datetime(6) DEFAULT NULL,
                              `updated_at` datetime(6) DEFAULT NULL,
                              `url` varchar(255) DEFAULT NULL,
                              `room_id` int DEFAULT NULL,
                              PRIMARY KEY (`img_id`),
                              KEY `FKcme41omxvwoj00bhqk7fwt70v` (`room_id`),
                              CONSTRAINT `FKcme41omxvwoj00bhqk7fwt70v` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_image`
--

LOCK TABLES `room_image` WRITE;
/*!40000 ALTER TABLE `room_image` DISABLE KEYS */;
INSERT INTO `room_image` VALUES (1,'2025-04-09 09:26:40.827017',NULL,'4.png',6),(2,'2025-04-09 09:26:40.875666',NULL,'5.png',6),(3,'2025-04-09 10:05:23.593203',NULL,'7.png',7),(4,'2025-04-09 10:05:23.617860',NULL,'6.png',7),(5,'2025-04-09 10:33:17.134868',NULL,'CHIBI FUNKY 4-min.png',8),(6,'2025-04-09 10:33:17.165090',NULL,'CHIBI FUNKY 7-min.png',8),(7,'2025-04-09 10:41:10.009290',NULL,'INDUSTRIAL 4-min.png',9),(8,'2025-04-09 10:41:10.035174',NULL,'CHIBI FUNKY 3-min.png',9);
/*!40000 ALTER TABLE `room_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_service`
--

DROP TABLE IF EXISTS `room_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_service` (
                                `service_id` int NOT NULL,
                                `room_id` int NOT NULL,
                                KEY `FK3jb3uo6oi9pyw63s0ulnlss32` (`room_id`),
                                KEY `FKn5ff8ekvf2ayw3o35ctksyfyp` (`service_id`),
                                CONSTRAINT `FK3jb3uo6oi9pyw63s0ulnlss32` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`),
                                CONSTRAINT `FKn5ff8ekvf2ayw3o35ctksyfyp` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_service`
--

LOCK TABLES `room_service` WRITE;
/*!40000 ALTER TABLE `room_service` DISABLE KEYS */;
INSERT INTO `room_service` VALUES (1,1),(2,1),(3,1),(1,2),(4,2),(1,3),(2,3),(5,3),(1,4),(3,5),(6,5),(1,6),(2,6),(1,7),(2,7),(1,8),(2,8),(1,9),(2,9);
/*!40000 ALTER TABLE `room_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
                           `service_id` int NOT NULL AUTO_INCREMENT,
                           `created_at` datetime(6) DEFAULT NULL,
                           `updated_at` datetime(6) DEFAULT NULL,
                           `price` double DEFAULT NULL,
                           `service_name` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES (1,NULL,NULL,NULL,'wifi'),(2,NULL,NULL,NULL,'air_conditioning'),(3,NULL,NULL,NULL,'pool'),(4,NULL,NULL,NULL,'gym'),(5,NULL,NULL,NULL,'restaurant'),(6,NULL,NULL,NULL,'spa'),(7,NULL,NULL,NULL,'bar'),(8,NULL,NULL,NULL,'conference_room');
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
                        `user_id` int NOT NULL AUTO_INCREMENT,
                        `created_at` datetime(6) DEFAULT NULL,
                        `updated_at` datetime(6) DEFAULT NULL,
                        `email` varchar(255) DEFAULT NULL,
                        `full_name` varchar(255) DEFAULT NULL,
                        `password` varchar(255) DEFAULT NULL,
                        `phone` varchar(255) DEFAULT NULL,
                        `score` int DEFAULT NULL,
                        `username` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`user_id`),
                        UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,NULL,NULL,'user@example.com','USER','$2a$10$eM5Xix35jMo7m55ARfkVd.dzEkh01fpmS8ueNV3EgEB4GG5anYtya','0123456789',1000,'user'),(2,NULL,NULL,'admin@example.com','ADMIN','$2a$10$NRJWWWdiafPX9MyKQBoXiep7OSlnGEBzrenEbubeWt0eGdbi2E6LS','0123456289',1000,'admin'),(3,NULL,NULL,'owner@example.com','OWNER','$2a$10$a.c7SBI08BxCHbZtPlAGDOh8hHWfk22ugSm0sMaN6RYOv853o3BYi','0123456782',1000,'owner'),(4,NULL,NULL,'user1@example.com',NULL,'$2a$10$SvP6dbNNFEh5Nb1u21Gmqevy18pEzGuigz4z3.dWRCruusbzHrm92','1234567299',1000,'user1'),(6,NULL,NULL,'user2@example.com',NULL,'$2a$10$GzuZjt2ykm1KBexDL5RhOeYX50k4gmy76H4ls5XS8z.fNwcUU.XP6','1234567299',0,'user2'),(8,NULL,NULL,'user4@example.com','New User','$2a$10$NLT330p9dftmgZhgUoyJIO/jPLIHw/dLXyO30r0Ebkwqt2PmfGbw.','1234567299',0,'user4'),(10,NULL,NULL,'user5@example.com','New User','$2a$10$.8ipSCdN0qfLSZRcz1mbS.YdbELYfztMOGuJ8r0JjhUozoYySQe7S','1234567299',0,'user5'),(11,NULL,NULL,'user6@example.com','New User','$2a$10$xTcdRoKNmnX8S0bbTpi9i.IwdbV9H.SWzOJLJjTvCEFkAUQtmcWDW','1234567299',0,'user6'),(12,NULL,NULL,'user7@example.com','New User','$2a$10$4q6RKfsWg72HACHZ696AFuFg6TJfFuanXbSlqUa..amt9Ql4lQ8UC','1234567299',0,'user8'),(13,NULL,NULL,'user729@example.com','New User','$2a$10$ai9dTRVp1IndOkbfli9B4eVfvIwrzFGd8DLOZNOjSKqe98.onof0O','1234567299',0,'user9'),(14,'2025-04-05 18:36:26.260942',NULL,'user729@example.com','New User','$2a$10$BtmchMDy4iWU6fRsnvrhCOBMriuS0V24rvusOkxTRNfrDZ9u6zVp2','1234567299',0,'user92');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
                             `user_id` int NOT NULL,
                             `role_id` int NOT NULL,
                             KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
                             KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
                             CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
                             CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1),(2,2),(3,3);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-18 22:54:41
