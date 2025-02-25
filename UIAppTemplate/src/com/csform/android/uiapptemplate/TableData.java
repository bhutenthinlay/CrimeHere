/*
 * this class contain information of database 
 */
package com.csform.android.uiapptemplate;

import android.provider.BaseColumns;

public class TableData {

	public TableData()
	{

	}

	public static abstract class TableInfo implements BaseColumns
	{
		public static final String ID="id";
		public static final String IDENTIFIER="identifier";
		public static final String TITLE="title";
		public static final String DESCRIPTION="description";
		public static final String TYPE_NAME="type_name";
		public static final String ADDRESS="address";
		public static final String LAT="lat";
		public static final String LNG="lng";
		public static final String IMAGE0="image0";
		public static final String IMAGE1="image1";
		public static final String IMAGE2="image2";
		public static final String USER_LOGIN="user_login";
		public static final String DATE_OCCURED="date_occured";
		public static final String TABLE_NAME="all_crimes";
		public static final String TABLE_NAME_CATEGORY="category";
		public static final String DATABASE_NAME="crime_here";
		public static final String IDCATEGORY="id";
		public static final String TYPE_NAME_CATEGORY="type_name";
	}

}