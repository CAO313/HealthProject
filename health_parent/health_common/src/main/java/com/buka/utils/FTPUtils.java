package com.buka.utils;

import org.apache.commons.net.ftp.*;
import java.io.*;
import java.net.*;
public class FTPUtils {

        //ftp服务器地址
        private String hostname = "101.34.14.236";
        //ftp服务器端口号默认为21
        private Integer port = 21 ;
        //ftp登录账号
        private String username = "admin";
        //ftp登录密码
        private String password = "cao410222?";

        private FTPClient ftpClient = null;

        private static String encoding = System.getProperty("file.encoding");


        public static void main(String[] args) throws IOException {
            FTPUtils fi = new FTPUtils();
            fi.initFtpClient();
            //fi.createDirecroty("2019");
            boolean flag = fi.uploadFile("/home/www/pages","39oij92po3j4234242423.jpg","D:\\QQ图片20210927201152.jpg");
            System.out.println(flag);
        }


        /**
         * 初始化ftp服务器
         */
        public void initFtpClient() {
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("utf-8");
            try {
                ftpClient.connect(hostname, port); //连接ftp服务器
                ftpClient.login(username, password);//登录ftp服务器
                ftpClient.enterLocalPassiveMode();//文件上传成功 但是内容为空 需要加上
                ftpClient.setControlEncoding(encoding);
                ftpClient.getReplyCode(); //是否成功登录服务器
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /**
         * 上传文件
         *
         * @param pathname       ftp服务保存地址
         * @param fileName       上传到ftp的文件名
         * @param originfilename 待上传文件的名称（绝对地址） *
         * @return
         */
        public boolean uploadFile(String pathname, String fileName, String originfilename) {
            boolean flag = false;
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(new File(originfilename));
                initFtpClient();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                if (!existFile(pathname)) {
                    createDirecroty(pathname);
                }
                ftpClient.changeWorkingDirectory(pathname);
                flag = ftpClient.storeFile(fileName, inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ftpClient.logout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != inputStream) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return flag;
        }

        /**
         * 上传文件
         *
         * @param pathname    ftp服务保存地址
         * @param fileName    上传到ftp的文件名
         * @param inputStream 输入文件流
         * @return
         */
        public boolean uploadFile(String pathname, String fileName, InputStream inputStream) {
            boolean flag = false;
            try {
                initFtpClient();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                if (!existFile(pathname)) {
                    createDirecroty(pathname);
                }
                ftpClient.changeWorkingDirectory(pathname);
                ftpClient.storeFile(fileName, inputStream);
                inputStream.close();
                ftpClient.logout();
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != inputStream) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }

        //改变目录路径
        public boolean changeWorkingDirectory(String directory) {
            boolean flag = true;
            try {
                flag = ftpClient.changeWorkingDirectory(directory);
                if (flag) {
                    System.out.println("进入文件夹" + directory + " 成功！");

                } else {
                    System.out.println("进入文件夹" + directory + " 失败！开始创建文件夹");
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return flag;
        }

        //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
        public boolean createDirecroty(String remote) throws IOException {
            String directory = remote + "/";
            // 如果远程目录不存在，则递归创建远程服务器目录
            if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(directory)) {
                int start = 0;
                int end = 0;
                if (directory.startsWith("/")) {
                    start = 1;
                }
                end = directory.indexOf("/", start);
                String path = "";
                StringBuilder paths = new StringBuilder();
                while (true) {
                    String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                    path = path + "/" + subDirectory;
                    if (!existFile(path)) {
                        if (makeDirectory(subDirectory)) {
                            changeWorkingDirectory(subDirectory);
                        } else {
                            System.out.println("创建目录[" + subDirectory + "]失败");
                            changeWorkingDirectory(subDirectory);
                        }
                    } else {
                        changeWorkingDirectory(subDirectory);
                    }
                    paths.append("/").append(subDirectory);
                    start = end + 1;
                    end = directory.indexOf("/", start);
                    // 检查所有目录是否创建完毕
                    if (end <= start) {
                        break;
                    }
                }
            }
            return true;
        }

        //判断ftp服务器文件是否存在
        public boolean existFile(String path) throws IOException {
            boolean flag = false;
            FTPFile[] ftpFileArr = ftpClient.listFiles(path);
            if (ftpFileArr.length > 0) {
                flag = true;
            }
            return flag;
        }

        //创建目录
        public boolean makeDirectory(String dir) {
            boolean flag = true;
            try {
                flag = ftpClient.makeDirectory(dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return flag;
        }

        /**
         * 下载文件 *
         *
         * @param pathname  FTP服务器文件目录 *
         * @param filename  文件名称 *
         * @param localpath 下载后的文件路径 *
         * @return
         */
        public boolean downloadFile(String pathname, String filename, String localpath) {
            boolean flag = false;
            OutputStream os = null;
            try {
                initFtpClient();
                //切换FTP目录
                ftpClient.changeWorkingDirectory(pathname);
                FTPFile[] ftpFiles = ftpClient.listFiles();
                for (FTPFile file : ftpFiles) {
                    if (filename.equalsIgnoreCase(file.getName())) {
                        File localFile = new File(localpath + "/" + file.getName());
                        os = new FileOutputStream(localFile);
                        ftpClient.retrieveFile(file.getName(), os);
                        os.close();
                    }
                }
                ftpClient.logout();
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return flag;
        }

        /**
         * 删除文件 *
         *
         * @param pathname FTP服务器保存目录 *
         * @param filename 要删除的文件名称 *
         * @return
         */
        public boolean deleteFile(String pathname, String filename) {
            boolean flag = false;
            try {
                initFtpClient();
                //切换FTP目录
                ftpClient.changeWorkingDirectory(pathname);
                ftpClient.dele(filename);
                ftpClient.logout();
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return flag;
        }

}
