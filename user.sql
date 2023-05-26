--SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for absent
-- ----------------------------
DROP TABLE IF EXISTS absent;
CREATE TABLE absent (
  "Sno" varchar(20) NOT NULL,
  "Sname" varchar(20) NOT NULL,
  "Dno" varchar(6) NOT NULL,
  "Atime" date DEFAULT NULL,
  "Areason" varchar(20) DEFAULT NULL,
  PRIMARY KEY ("Sno")
);

-- ----------------------------
-- Records of absent
-- ----------------------------

-- ----------------------------
-- Table structure for building
-- ----------------------------
DROP TABLE IF EXISTS building;
CREATE TABLE building (
  "Bname" varchar(20) NOT NULL,
  "Bfloor" varchar(10) NOT NULL,
  "Bmager" varchar(10) DEFAULT NULL,
  "Bcap" varchar(10) NOT NULL,
  PRIMARY KEY ("Bname")
) ;

-- ----------------------------
-- Records of building
-- ----------------------------

-- ----------------------------
-- Table structure for dorm
-- ----------------------------
DROP TABLE IF EXISTS dorm;
CREATE TABLE dorm (
  "Dno" varchar(10) NOT NULL,
  "Dpo" varchar(10) NOT NULL,
  "Dphone" varchar(20) DEFAULT NULL,
  "Dcap" varchar(10) NOT NULL,
  PRIMARY KEY ("Dno")
) ;

-- ----------------------------
-- Records of dorm
-- ----------------------------

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS student;
CREATE TABLE student (
  "Sno" varchar(20) NOT NULL,
  "Sname" varchar(20) NOT NULL,
  "Ssex" varchar(4) NOT NULL,
  "Sdept" varchar(40) NOT NULL,
  "Dno" varchar(6) DEFAULT NULL,
  "Bbu" varchar(20) DEFAULT NULL,
  PRIMARY KEY ("Sno")
) ;

-- ----------------------------
-- Records of student
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS users;
CREATE TABLE users (
  "Uname" varchar(20) NOT NULL,
  "Upassword" varchar(20) NOT NULL,
  "Utype" int NOT NULL,
   PRIMARY KEY ("Uname")
);

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO users VALUES ('root', '1026', 3),
('孙悟空','swk',2),
('陶万启', 'twq', 1);
