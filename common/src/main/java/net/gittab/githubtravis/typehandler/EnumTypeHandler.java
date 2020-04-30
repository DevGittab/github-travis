package net.gittab.githubtravis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * EnumTypeHandler.
 *
 * @param <E> enum
 * @author xiaohua zhou
 **/
public class EnumTypeHandler<E extends Enum<E> & DbEnumCode>
		extends BaseTypeHandler<DbEnumCode> {

	private Class<E> enumClass;

	public EnumTypeHandler(Class<E> enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	public void setNonNullParameter(PreparedStatement preparedStatement, int i,
			DbEnumCode enumCode, JdbcType jdbcType) throws SQLException {
		preparedStatement.setInt(i, enumCode.getCode());
	}

	@Override
	public DbEnumCode getNullableResult(ResultSet resultSet, String columnName)
			throws SQLException {
		int status = resultSet.getInt(columnName);
		if (resultSet.wasNull() && status == 0) {
			return null;
		}
		return convert(status);
	}

	@Override
	public DbEnumCode getNullableResult(ResultSet resultSet, int columnIndex)
			throws SQLException {
		int status = resultSet.getInt(columnIndex);
		if (resultSet.wasNull() && status == 0) {
			return null;
		}
		return convert(status);
	}

	@Override
	public DbEnumCode getNullableResult(CallableStatement callableStatement,
			int columnIndex) throws SQLException {
		int status = callableStatement.getInt(columnIndex);
		if (callableStatement.wasNull() && status == 0) {
			return null;
		}
		return convert(status);
	}

	private E convert(int code) {
		E[] enumConstants = this.enumClass.getEnumConstants();
		for (E e : enumConstants) {
			if (e.getCode() == code) {
				return e;
			}
		}
		return null;
	}

}
