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
        createdDate DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
        isActive BIT NOT NULL CONSTRAINT DF_Users_isActive DEFAULT 1,
        activationCodeHash NVARCHAR(255) NULL,
        activationExpiresAt DATETIME2 NULL,
        resetCodeHash NVARCHAR(255) NULL,
        resetExpiresAt DATETIME2 NULL,
        profilePendingEmail NVARCHAR(255) NULL,
        profileEmailOtpHash NVARCHAR(255) NULL,
        profileEmailOtpExpiresAt DATETIME2 NULL
    );
END;
GO

-- Bổ sung các cột cho database đã được tạo trước đó.
IF COL_LENGTH(N'dbo.Users', N'isActive') IS NULL
    ALTER TABLE dbo.Users ADD isActive BIT NOT NULL
        CONSTRAINT DF_Users_isActive_Migration DEFAULT 1 WITH VALUES;
GO
IF COL_LENGTH(N'dbo.Users', N'activationCodeHash') IS NULL
    ALTER TABLE dbo.Users ADD activationCodeHash NVARCHAR(255) NULL;
GO
IF COL_LENGTH(N'dbo.Users', N'activationExpiresAt') IS NULL
    ALTER TABLE dbo.Users ADD activationExpiresAt DATETIME2 NULL;
GO
IF COL_LENGTH(N'dbo.Users', N'resetCodeHash') IS NULL
    ALTER TABLE dbo.Users ADD resetCodeHash NVARCHAR(255) NULL;
GO
IF COL_LENGTH(N'dbo.Users', N'resetExpiresAt') IS NULL
    ALTER TABLE dbo.Users ADD resetExpiresAt DATETIME2 NULL;
GO
IF COL_LENGTH(N'dbo.Users', N'profilePendingEmail') IS NULL
    ALTER TABLE dbo.Users ADD profilePendingEmail NVARCHAR(255) NULL;
GO
IF COL_LENGTH(N'dbo.Users', N'profileEmailOtpHash') IS NULL
    ALTER TABLE dbo.Users ADD profileEmailOtpHash NVARCHAR(255) NULL;
GO
IF COL_LENGTH(N'dbo.Users', N'profileEmailOtpExpiresAt') IS NULL
    ALTER TABLE dbo.Users ADD profileEmailOtpExpiresAt DATETIME2 NULL;
GO

UPDATE dbo.Users
SET roleid = 1
WHERE username = N'admin';

IF OBJECT_ID(N'dbo.Videos', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Videos (
        video_id INT IDENTITY(1,1) PRIMARY KEY,
        title NVARCHAR(255) NOT NULL,
        description NVARCHAR(MAX) NULL,
        file_name NVARCHAR(255) NOT NULL UNIQUE,
        mime_type NVARCHAR(100) NOT NULL,
        created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
        user_id INT NOT NULL,
        CONSTRAINT FK_Videos_Users FOREIGN KEY (user_id)
            REFERENCES dbo.Users(id) ON DELETE CASCADE
    );
END;
GO


