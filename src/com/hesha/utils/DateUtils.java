package com.hesha.utils;

/*第一步 初始化:
	http://192.168.1.45:8080/datasync/services/rest/syncWebService/clientInitSync.json
	Content type: application/json
	{
		"ClientInitPackageBean": {
			"appName": "gigatalk",
			"lastSyncGMTTime": "0",
			"password": "111111",
			"syncMode": 0,
			"transmission": 0,
			"username": 26
		}
	}

	lastSyncGMTTime:客户端上一次同步的格林威治时间戳，由服务器传送给客户端，第一次同步值为："0"
	syncMode:同步方式 0:合并同步 2:覆盖服务器数据 3:覆盖客户端数据
	username:为身份标识。gigatalk项目中为：user_id

	服务器返回值：
	{"ServerInitPackageBean":{"syncMode":0,"uuid":"9237b6fb-7188-4db4-a015-54c0fe161f65"}}

	syncMode:确认到本次同步所采用的同步方式
	uuid:一次同步的标识

	第二步 发送数据：
	http://192.168.1.45:8080/datasync/services/rest/syncWebService/sendData.json
	Content type: application/json
	{
		"ClientDatapackageBean": {
			"syncMode": 0,
			"uuid": "2c868a46-56f3-4174-a711-39d7ffa13bf0",
			"userName": 26,
			"appName": "gigatalk",
			"currentGMTTimeLong": 1314314007669,
			"lastSyncGMTTimeLong": 1210511956,
			"database": {
				"databaseName": "giga_talk",
				"table": [
					{
						"tableName": "contacts",
						"operate": [
							{
								"operateName": "insert",
								"codeList": [
									{
										"Map": {
											"entry": [
												{
													"key": "user_id",
													"value": 26
												}, {
													"key": "city",
													"value": "testcity1"
												}, {
													"key": "created",
													"value": 1314264508
												}, {
													"key": "updated",
													"value": 1314264501
												}, {
													"key": "sync_id",
													"value": "11111111-a111-1a11-1a1a-111111111111"
												}
											]
										}
									}, {
										"Map": {
											"entry": [
												{
													"key": "user_id",
													"value": 26
												}, {
													"key": "city",
													"value": "testcity2"
												}, {
													"key": "created",
													"value": 1314264508
												}, {
													"key": "updated",
													"value": 1314264502
												}, {
													"key": "sync_id",
													"value": "22222222-a222-2a22-2a2a-222222222222"
												}
											]
										}
									}
								]// operate end
							}, {
								"operateName": "update",
								"codeList": [
									{
										"Map": {
											"entry": [
												{
													"key": "user_id",
													"value": 26
												}, {
													"key": "city",
													"value": "testupdate1"
												}, {
													"key": "updated",
													"value": 1314264503
												}, {
													"key": "sync_id",
													"value": "33333333-a333-3a33-3a3a-333333333333"
												}
											]
										}
									}, {
										"Map": {
											"entry": [
												{
													"key": "user_id",
													"value": 26
												}, {
													"key": "city",
													"value": "test13323"
												}, {
													"key": "updated",
													"value": 1314264504
												}, {
													"key": "sync_id",
													"value": "44444444-a444-4a44-4a4a-444444444444"
												}
											]
										}
									}
								]// operate end
							}, {
								"operateName": "delete",
								"codeList": [
									{
										"Map": {
											"entry": [
												{
													"key": "user_id",
													"value": 26
												}, {
													"key": "updated",
													"value": 1314264504
												}, {
													"key": "sync_id",
													"value": "55555555-a555-5a55-5a5a-555555555555"
												}
											]
										}
									}, {
										"Map": {
											"entry": [
												{
													"key": "user_id",
													"value": 26
												}, {
													"key": "updated",
													"value": 1314264504
												}, {
													"key": "sync_id",
													"value": "66666666-a666-6a66-6a6a-666666666666"
												}
											]
										}
									}
								]
							}
						]// operate end
					}, {
						"tableName": "user_tag",
						"operate": [
							{
								"operateName": "insert",
								"codeList": [
									{
										"Map": {
											"entry": [
												{
													"key": "user_id",
													"value": 26
												}, {
													"key": "tag_name",
													"value": "test22"
												}, {
													"key": "contacts",
													"value": "bdd"
												}, {
													"key": "created",
													"value": 1314264508
												}, {
													"key": "updated",
													"value": 1314264508
												}, {
													"key": "sync_id",
													"value": "77777777-a777-7a77-7a7a-777777777777"
												}
											]
										}
									}, {
										"Map": {
											"entry": [
												{
													"key": "user_id",
													"value": 26
												}, {
													"key": "tag_name",
													"value": "test22"
												}, {
													"key": "contacts",
													"value": "sss"
												}, {
													"key": "created",
													"value": 1314264508
												}, {
													"key": "updated",
													"value": 1314264508
												}, {
													"key": "sync_id",
													"value": "88888888-a888-8a88-8a8a-888888888888"
												}
											]
										}
									}
								]
							}, {
								"operateName": "delete",
								"codeList": [
									{
										"Map": {
											"entry": [
												{
													"key": "user_id",
													"value": 26
												}, {
													"key": "updated",
													"value": 1314264504
												}, {
													"key": "sync_id",
													"value": "99999999-a999-9a99-9a9a-999999999999"
												}
											]
										}
									}, {
										"Map": {
											"entry": [
												{
													"key": "user_id",
													"value": 26
												}, {
													"key": "updated",
													"value": 1314264504
												}, {
													"key": "sync_id",
													"value": "00000000-a000-0a00-0a0a-000000000000"
												}
											]
										}
									}
								]
							}
						]
					}
				]
			}
		}
	}@md5
	返回结果：
	{
		"ServerDataPackageBean": {
			"lastSyncGMTTimeLong": 1314314032,
			"database": {
				"databaseName": "giga_talk",
				"table": [
					{
						"tableName": "user_tag",
						"operate": {
							"operateName": "insertOrUpdate",
							"codeList": [
								{
									"Map": {
										"entry": [
											{
												"key": "sync_id",
												"value": "77777777-a777-7a77-7a7a-777777777777"
											}, {
												"key": "updated",
												"value": 1314264508
											}, {
												"key": "created",
												"value": 1314264508
											}, {
												"key": "color",
												"value": 0
											}, {
												"key": "user_id",
												"value": 26
											}, {
												"key": "tag_name",
												"value": "test22"
											}, {
												"key": "local_tag"
											}, {
												"key": "contacts",
												"value": "bdd"
											}
										]
									}
								}, {
									"Map": {
										"entry": [
											{
												"key": "sync_id",
												"value": "88888888-a888-8a88-8a8a-888888888888"
											}, {
												"key": "updated",
												"value": 1314264508
											}, {
												"key": "created",
												"value": 1314264508
											}, {
												"key": "color",
												"value": 0
											}, {
												"key": "user_id",
												"value": 26
											}, {
												"key": "tag_name",
												"value": "test22_1"
											}, {
												"key": "local_tag"
											}, {
												"key": "contacts",
												"value": "sss"
											}
										]
									}
								}
							]
						}
					}, {
						"tableName": "contacts",
						"operate": {
							"operateName": "insertOrUpdate",
							"codeList": [
								{
									"Map": {
										"entry": [
											{
												"key": "fax_country_code"
											}, {
												"key": "birth"
											}, {
												"key": "email_one"
											}, {
												"key": "mobile_phone_two"
											}, {
												"key": "home_area_code"
											}, {
												"key": "home_country_code"
											}, {
												"key": "state"
											}, {
												"key": "fax_phone"
											}, {
												"key": "city",
												"value": "testupdate1"
											}, {
												"key": "mobile_country_code_two"
											}, {
												"key": "first_name"
											}, {
												"key": "title"
											}, {
												"key": "updated",
												"value": 1314264503
											}, {
												"key": "created",
												"value": 1314261819
											}, {
												"key": "work_country_code"
											}, {
												"key": "gender"
											}, {
												"key": "fax_area_code"
											}, {
												"key": "user_id",
												"value": 26
											}, {
												"key": "work_area_code"
											}, {
												"key": "sync_id",
												"value": "33333333-a333-3a33-3a3a-333333333333"
											}, {
												"key": "mobile_area_code_one"
											}, {
												"key": "picture_url"
											}, {
												"key": "mobile_country_code_one"
											}, {
												"key": "nickname"
											}, {
												"key": "website"
											}, {
												"key": "device_id",
												"value": 1234567895421321
											}, {
												"key": "corder"
											}, {
												"key": "cid"
											}, {
												"key": "mobile_area_code_two"
											}, {
												"key": "country"
											}, {
												"key": "source"
											}, {
												"key": "address"
											}, {
												"key": "company"
											}, {
												"key": "master_id",
												"value": 0
											}, {
												"key": "email_two"
											}, {
												"key": "last_name"
											}, {
												"key": "work_phone"
											}, {
												"key": "home_phone"
											}, {
												"key": "mobile_phone_one"
											}, {
												"key": "work_extension"
											}
										]
									}
								}, {
									"Map": {
										"entry": [
											{
												"key": "fax_country_code"
											}, {
												"key": "birth"
											}, {
												"key": "email_one"
											}, {
												"key": "mobile_phone_two"
											}, {
												"key": "home_area_code"
											}, {
												"key": "home_country_code"
											}, {
												"key": "state"
											}, {
												"key": "fax_phone"
											}, {
												"key": "city",
												"value": "test13323"
											}, {
												"key": "mobile_country_code_two"
											}, {
												"key": "first_name"
											}, {
												"key": "title"
											}, {
												"key": "updated",
												"value": 1314264504
											}, {
												"key": "created",
												"value": 1314261819
											}, {
												"key": "work_country_code"
											}, {
												"key": "gender"
											}, {
												"key": "fax_area_code"
											}, {
												"key": "user_id",
												"value": 26
											}, {
												"key": "work_area_code"
											}, {
												"key": "sync_id",
												"value": "44444444-a444-4a44-4a4a-444444444444"
											}, {
												"key": "mobile_area_code_one"
											}, {
												"key": "picture_url"
											}, {
												"key": "mobile_country_code_one"
											}, {
												"key": "nickname"
											}, {
												"key": "website"
											}, {
												"key": "device_id",
												"value": 1234567895421321
											}, {
												"key": "corder"
											}, {
												"key": "cid"
											}, {
												"key": "mobile_area_code_two"
											}, {
												"key": "country"
											}, {
												"key": "source"
											}, {
												"key": "address"
											}, {
												"key": "company"
											}, {
												"key": "master_id",
												"value": 0
											}, {
												"key": "email_two"
											}, {
												"key": "last_name"
											}, {
												"key": "work_phone"
											}, {
												"key": "home_phone"
											}, {
												"key": "mobile_phone_one"
											}, {
												"key": "work_extension"
											}
										]
									}
								}, {
									"Map": {
										"entry": [
											{
												"key": "fax_country_code"
											}, {
												"key": "birth"
											}, {
												"key": "email_one"
											}, {
												"key": "mobile_phone_two"
											}, {
												"key": "home_area_code"
											}, {
												"key": "home_country_code"
											}, {
												"key": "state"
											}, {
												"key": "fax_phone"
											}, {
												"key": "city",
												"value": "testcity1"
											}, {
												"key": "mobile_country_code_two"
											}, {
												"key": "first_name"
											}, {
												"key": "title"
											}, {
												"key": "updated",
												"value": 1314264501
											}, {
												"key": "created",
												"value": 1314264508
											}, {
												"key": "work_country_code"
											}, {
												"key": "gender"
											}, {
												"key": "fax_area_code"
											}, {
												"key": "user_id",
												"value": 26
											}, {
												"key": "work_area_code"
											}, {
												"key": "sync_id",
												"value": "11111111-a111-1a11-1a1a-111111111111"
											}, {
												"key": "mobile_area_code_one"
											}, {
												"key": "picture_url"
											}, {
												"key": "mobile_country_code_one"
											}, {
												"key": "nickname"
											}, {
												"key": "website"
											}, {
												"key": "device_id",
												"value": 1234567895421321
											}, {
												"key": "corder"
											}, {
												"key": "cid"
											}, {
												"key": "mobile_area_code_two"
											}, {
												"key": "country"
											}, {
												"key": "source"
											}, {
												"key": "address"
											}, {
												"key": "company"
											}, {
												"key": "master_id",
												"value": 0
											}, {
												"key": "email_two"
											}, {
												"key": "last_name"
											}, {
												"key": "work_phone"
											}, {
												"key": "home_phone"
											}, {
												"key": "mobile_phone_one"
											}, {
												"key": "work_extension"
											}
										]
									}
								}, {
									"Map": {
										"entry": [
											{
												"key": "fax_country_code"
											}, {
												"key": "birth"
											}, {
												"key": "email_one"
											}, {
												"key": "mobile_phone_two"
											}, {
												"key": "home_area_code"
											}, {
												"key": "home_country_code"
											}, {
												"key": "state"
											}, {
												"key": "fax_phone"
											}, {
												"key": "city",
												"value": "testcity2"
											}, {
												"key": "mobile_country_code_two"
											}, {
												"key": "first_name"
											}, {
												"key": "title"
											}, {
												"key": "updated",
												"value": 1314264502
											}, {
												"key": "created",
												"value": 1314264508
											}, {
												"key": "work_country_code"
											}, {
												"key": "gender"
											}, {
												"key": "fax_area_code"
											}, {
												"key": "user_id",
												"value": 26
											}, {
												"key": "work_area_code"
											}, {
												"key": "sync_id",
												"value": "22222222-a222-2a22-2a2a-222222222222"
											}, {
												"key": "mobile_area_code_one"
											}, {
												"key": "picture_url"
											}, {
												"key": "mobile_country_code_one"
											}, {
												"key": "nickname"
											}, {
												"key": "website"
											}, {
												"key": "device_id",
												"value": 1234567895421321
											}, {
												"key": "corder"
											}, {
												"key": "cid"
											}, {
												"key": "mobile_area_code_two"
											}, {
												"key": "country"
											}, {
												"key": "source"
											}, {
												"key": "address"
											}, {
												"key": "company"
											}, {
												"key": "master_id",
												"value": 0
											}, {
												"key": "email_two"
											}, {
												"key": "last_name"
											}, {
												"key": "work_phone"
											}, {
												"key": "home_phone"
											}, {
												"key": "mobile_phone_one"
											}, {
												"key": "work_extension"
											}
										]
									}
								}
							]
						}
					}, {
						"tableName": "contactsDeleteLog",
						"operate": {
							"operateName": "delete",
							"codeList": [
								{
									"Map": {
										"entry": [
											{
												"key": "sync_id",
												"value": "55555555-a555-5a55-5a5a-555555555555"
											}, {
												"key": "priority",
												"value": 1
											}
										]
									}
								}, {
									"Map": {
										"entry": [
											{
												"key": "sync_id",
												"value": "66666666-a666-6a66-6a6a-666666666666"
											}, {
												"key": "priority",
												"value": 1
											}
										]
									}
								}
							]
						}
					}, {
						"tableName": "user_tag",
						"operate": {
							"operateName": "delete",
							"codeList": [
								{
									"Map": {
										"entry": [
											{
												"key": "sync_id",
												"value": "99999999-a999-9a99-9a9a-999999999999"
											}, {
												"key": "priority",
												"value": 1
											}
										]
									}
								}, {
									"Map": {
										"entry": [
											{
												"key": "sync_id",
												"value": "00000000-a000-0a00-0a0a-000000000000"
											}, {
												"key": "priority",
												"value": 1
											}
										]
									}
								}
							]
						}
					}
				]
			}
		}
	}

	第三步 确认数据
	http://192.168.1.45:8080/datasync/services/rest/syncWebService/clientSyncStatus.json
	Content type: application/json
	{"ClientSyncStatusBean":{"uuid":"9237b6fb-7188-4db4-a015-54c0fe161f65","status":"syncSuccess"}}

	覆盖客户端的第一步：
	{
		"ClientInitPackageBean": {
			"appName": "gigatalk",
			"lastSyncGMTTime": "0",
			"password": "111111",
			"syncMode": 3,
			"transmission": 0,
			"username": 26
		}
	}
	覆盖客户端的第二步：
	{
		"ClientDatapackageBean": {
			"syncMode": 3,
			"uuid": "14980dc5-2fed-4462-b312-b1e11452b590",
			"userName": 26,
			"appName": "gigatalk",
			"currentGMTTimeLong": 1314315264532,
			"lastSyncGMTTimeLong": 1210511956,
			"database": {
				"databaseName": "giga_talk",
				"table": [
					{
						"tableName": "contacts"
					}, {
						"tableName": "user_tag"
					}
				]
			}
		}
	}@980f2c5a5f382d28f0240c55b164936c

	覆盖客户端的第一步：
	{
		"ClientInitPackageBean": {
			"appName": "gigatalk",
			"lastSyncGMTTime": "0",
			"password": "111111",
			"syncMode": 2,
			"transmission": 0,
			"username": 26
		}
	}
	覆盖客户端的第二步：
	{
		"ClientDatapackageBean": {
			"syncMode": 2,
			"uuid": "624b5923-a3f9-4ddc-a39c-7f787749bcd6",
			"userName": 26,
			"appName": "gigatalk",
			"currentGMTTimeLong": 1314318293407,
			"lastSyncGMTTimeLong": 1210511956,
			"database": {
				"databaseName": "giga_talk",
				"table": [
					{
						"tableName": "contacts",
						"operate": {
							"operateName": "insert",
							"codeList": [
								{
									"Map": {
										"entry": [
											{
												"key": "user_id",
												"value": 26
											}, {
												"key": "city",
												"value": "testcity1"
											}, {
												"key": "created",
												"value": 1314264508
											}, {
												"key": "updated",
												"value": 1314264501
											}, {
												"key": "sync_id",
												"value": "111111ab-a111-1a11-1a1a-111111111111"
											}
										]
									}
								}, {
									"Map": {
										"entry": [
											{
												"key": "user_id",
												"value": 26
											}, {
												"key": "city",
												"value": "testcity2"
											}, {
												"key": "created",
												"value": 1314264508
											}, {
												"key": "updated",
												"value": 1314264502
											}, {
												"key": "sync_id",
												"value": "222222ab-a222-2a22-2a2a-222222222222"
											}
										]
									}
								}
							]
						}
					}, {
						"tableName": "user_tag",
						"operate": {
							"operateName": "insert",
							"codeList": [
								{
									"Map": {
										"entry": [
											{
												"key": "user_id",
												"value": 26
											}, {
												"key": "tag_name",
												"value": "test22"
											}, {
												"key": "contacts",
												"value": "bdd"
											}, {
												"key": "created",
												"value": 1314264508
											}, {
												"key": "updated",
												"value": 1314264508
											}, {
												"key": "sync_id",
												"value": "777777ab-a777-7a77-7a7a-777777777777"
											}
										]
									}
								}, {
									"Map": {
										"entry": [
											{
												"key": "user_id",
												"value": 26
											}, {
												"key": "tag_name",
												"value": "test22"
											}, {
												"key": "contacts",
												"value": "sss"
											}, {
												"key": "created",
												"value": 1314264508
											}, {
												"key": "updated",
												"value": 1314264508
											}, {
												"key": "sync_id",
												"value": "888888ab-a888-8a88-8a8a-888888888888"
											}
										]
									}
								}
							]
						}
					}
				]
			}
		}
	}*/


import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

public class DateUtils {
	public static String getNow() {
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setCalendar(cal);
	    String GMTDate = formatter.format(cal.getTime());
		return GMTDate;								  
	}

	public static String getGregorianTimeBefore(int before) {

		long mTime = System.currentTimeMillis();
		int offset = Calendar.getInstance().getTimeZone().getRawOffset();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(mTime - offset));

		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		c.set(Calendar.DATE, c.get(Calendar.DATE) - before);
		String bTime = simp.format(c.getTime());
		return bTime;
	}
	
	public static String getNowDate() {
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setCalendar(cal);
	    String GMTDate = formatter.format(cal.getTime());
		return GMTDate;								  
	}
	
	public static String LocalToGMTString(Date localDate) {
		
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setCalendar(cal);
	    String GMTDate = formatter.format(localDate);
		return GMTDate;
	}
	
	public static String LocalToGMTString(Date localDate, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
	    String GMTDate = formatter.format(localDate);
		return GMTDate;
	}


	public static String GMTToLocalString(Date GMTDate) {
		
		long GMTMiliseconds = GMTDate.getTime();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(GMTMiliseconds);
		Date localTime = new Date(GMTMiliseconds + (cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET)/60 * 1000));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String localStr = formatter.format(localTime);
		return localStr;
	}

	public static Date StringToDate(String dateString) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = formatter.parse(dateString, new ParsePosition(0));
		return date;
	}

	public static String getTimeSinceNow(int secs) {
		
        long now = System.currentTimeMillis();    
        long offset = now + secs;
		GregorianCalendar cal = new GregorianCalendar();
		int gmtOff=cal.getTimeZone().getRawOffset();
		cal.setTimeInMillis(offset-gmtOff);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String localStr = formatter.format(cal.getTimeInMillis());
		return localStr;								  
	}
	
	public static Date getDateFromTimeMillis(Long timeMillis){
		Date date = null;
		try {
			date = new Date(timeMillis);
		} catch(Exception e){
			date = new Date();
		}
		return date;
	}
	
	/**
	 * 根据毫秒数取得时间字符串
	 * @param timeMillis
	 * @return
	 */
	public static String getStringFromTimeMillis(String timeMillis) {
		Long l = 0L;
		try {
			l = Long.valueOf(timeMillis);
		} catch (NumberFormatException e){
			l = System.currentTimeMillis();
		}
		Date date = getDateFromTimeMillis(l);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = format.format(date);
		return result;
	}
	
	public static String getStringFromTimeMillisWithOutSeconds(String timeMillis) {
		Long l = 0L;
		try {
			l = Long.valueOf(timeMillis);
		} catch (NumberFormatException e){
			l = System.currentTimeMillis();
		}
		Date date = getDateFromTimeMillis(l);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String result = format.format(date);
		return result;
	}
	
	/**
     * 获取系统当前格林威治日历
     * @return 当前格林威治日历
     */
    private static Calendar getCurrentGMTCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MILLISECOND, -cal.getTimeZone().getRawOffset());
        return cal;
    }
    /**
     * 获取系统当前格林威治时间
     * @return 当前格林威治时间 {@link Date}
     */
    private static Date getCurrentGMTDate() {
        return getCurrentGMTCalendar().getTime();
    }
    /**
     * 获取系统当前格林威治时间的毫秒值
     * @return 当前格林威治时间毫秒值
     */
    public static long getCurrentGMTMillisecond() {
        return getCurrentGMTDate().getTime();
    }
    
	
}
