package com.dao;

import com.bean.HotelBean;
import com.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HotelDao {

    public HotelDao() {
    }

    public List<HotelBean> getAllHotels() {
        List<HotelBean> hotels = new ArrayList<>();
        String sql = "SELECT h.hotel_id, h.legacy_id, h.hotel_name, h.location, h.rating, h.price_per_night, " +
                     "COALESCE(MIN(r.price), h.price_per_night) AS min_price, " +
                     "COALESCE(MAX(r.price), h.price_per_night * 1.5) AS max_price " +
                     "FROM hotels h " +
                     "LEFT JOIN rooms r ON h.hotel_id = r.hotel_id " +
                     "GROUP BY h.hotel_id, h.legacy_id, h.hotel_name, h.location, h.rating, h.price_per_night " +
                     "ORDER BY h.hotel_id ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                hotels.add(mapResultSetToHotel(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return hotels;
    }

    public HotelBean getHotelById(int id) {
        String sql = "SELECT h.hotel_id, h.legacy_id, h.hotel_name, h.location, h.rating, h.price_per_night, " +
                     "COALESCE(MIN(r.price), h.price_per_night) AS min_price, " +
                     "COALESCE(MAX(r.price), h.price_per_night * 1.5) AS max_price " +
                     "FROM hotels h " +
                     "LEFT JOIN rooms r ON h.hotel_id = r.hotel_id " +
                     "WHERE h.hotel_id = ? " +
                     "GROUP BY h.hotel_id, h.legacy_id, h.hotel_name, h.location, h.rating, h.price_per_night";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToHotel(rs);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return null;
    }

    private HotelBean mapResultSetToHotel(ResultSet rs) throws SQLException {
        HotelBean hotel = new HotelBean();
        hotel.setId(rs.getInt("hotel_id"));
        int legacyId = rs.getInt("legacy_id");
        if (!rs.wasNull()) {
            hotel.setLegacyId(legacyId);
        } else {
            hotel.setLegacyId(null);
        }
        hotel.setName(rs.getString("hotel_name"));
        hotel.setLocation(rs.getString("location"));
        hotel.setRating(rs.getDouble("rating"));
        hotel.setPricePerNight(rs.getDouble("price_per_night"));
        hotel.setMinPrice(rs.getDouble("min_price"));
        hotel.setMaxPrice(rs.getDouble("max_price"));
        return hotel;
    }

    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException ignored) {
            }
        }
        if (conn != null) {
            DBConnection.closeConnection(conn);
        }
    }
}
