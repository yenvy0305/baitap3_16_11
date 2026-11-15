CREATE DATABASE ShoppingService;
GO

USE ShoppingService;
GO

IF OBJECT_ID(N'dbo.Category', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Category (
        cate_id INT IDENTITY(1,1) PRIMARY KEY,
        cate_name NVARCHAR(255) NOT NULL,
        icons NVARCHAR(255) NULL
    );
END;
GO

IF OBJECT_ID(N'dbo.Users', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Users (
        id INT IDENTITY(1,1) PRIMARY KEY,
        email NVARCHAR(255) NOT NULL UNIQUE,
        username NVARCHAR(100) NOT NULL UNIQUE,
        fullname NVARCHAR(255) NOT NULL,
        [password] NVARCHAR(255) NOT NULL,
        avatar NVARCHAR(255) NULL,
        roleid INT NOT NULL DEFAULT 5,
        phone NVARCHAR(30) NULL,
        createdDate DATETIME2 NOT NULL DEFAULT SYSDATETIME()
    );
END;
GO

UPDATE dbo.Users
SET roleid = 1
WHERE username = N'admin';