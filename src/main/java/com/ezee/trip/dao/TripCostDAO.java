package com.ezee.trip.dao;

import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import com.ezee.trip.dto.TripCostDTO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.exception.ServiceException;
import com.ezee.trip.dto.TaxDTO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Cleanup;

@Repository
public class TripCostDAO {

    @Autowired
    private DataSource dataSource;

    public static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.dao");

    public int addTripCost(TripCostDTO tripCost) {
        int affected = 0;
        try {
            int pindex = 0;
            @Cleanup
            Connection connection = dataSource.getConnection();

            @Cleanup
            CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_TRIP_COST_IUD(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            callableStatement.setString(++pindex, tripCost.getCode());
            callableStatement.setString(++pindex, tripCost.getTripDTO().getCode());
            callableStatement.setBigDecimal(++pindex, tripCost.getAccomodationCost());
            callableStatement.setBigDecimal(++pindex, tripCost.getTravelCost());
            callableStatement.setBigDecimal(++pindex, tripCost.getFoodCost());
            callableStatement.setBigDecimal(++pindex, tripCost.getActivityCost());
            callableStatement.setBigDecimal(++pindex, tripCost.getServiceFees());
            callableStatement.setString(++pindex, tripCost.getTaxDTO().getCode());
            callableStatement.setInt(++pindex, tripCost.getActiveFlag());
            callableStatement.setString(++pindex, tripCost.getUpdatedBy());
            callableStatement.registerOutParameter(++pindex, Types.INTEGER);

            callableStatement.execute();
            affected = callableStatement.getInt(pindex);

        } catch (Exception e) {
            LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
        return affected;
    }

 
    public TripCostDTO getTripCost(TripCostDTO tripCost) {
        try {
            @Cleanup
            Connection connection = dataSource.getConnection();
            @Cleanup
            PreparedStatement statement = null;

            if (tripCost.getId() != 0) {
                String query = "SELECT id, code, trip_id, accomodation_cost, travel_cost, food_cost, activity_cost, service_fees, tax_id, active_flag, updated_by, updated_at FROM trip_cost WHERE id = ? AND active_flag = 1";
                statement = connection.prepareStatement(query);
                statement.setInt(1, tripCost.getId());
            } else {
                String query = "SELECT id, code, trip_id, accomodation_cost, travel_cost, food_cost, activity_cost, service_fees, tax_id, active_flag, updated_by, updated_at FROM trip_cost WHERE code = ? AND active_flag = 1";
                statement = connection.prepareStatement(query);
                statement.setString(1, tripCost.getCode());
            }

            @Cleanup
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                tripCost.setId(resultSet.getInt("id"));
                tripCost.setCode(resultSet.getString("code"));
                TripDTO tripDTO = new TripDTO();
                tripDTO.setId(resultSet.getInt("trip_id"));
                tripCost.setTripDTO(tripDTO);
                tripCost.setAccomodationCost(resultSet.getBigDecimal("accomodation_cost"));
                tripCost.setTravelCost(resultSet.getBigDecimal("travel_cost"));
                tripCost.setFoodCost(resultSet.getBigDecimal("food_cost"));
                tripCost.setActivityCost(resultSet.getBigDecimal("activity_cost"));
                tripCost.setServiceFees(resultSet.getBigDecimal("service_fees"));
                TaxDTO taxDTO = new TaxDTO();
                taxDTO.setId(resultSet.getInt("tax_id"));
                tripCost.setTaxDTO(taxDTO);
                tripCost.setActiveFlag(resultSet.getInt("active_flag"));
                tripCost.setUpdatedBy(resultSet.getString("updated_by"));
                tripCost.setUpdatedAt(resultSet.getString("updated_at"));
            }

        } catch (Exception e) {
            LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }

        return tripCost;
    }

  
    public List<TripCostDTO> getAllTripCosts() {
        List<TripCostDTO> list = new ArrayList<>();
        try {
            @Cleanup
            Connection connection = dataSource.getConnection();

            String query = "SELECT id, code, trip_id, accomodation_cost, travel_cost, food_cost, activity_cost, service_fees, tax_id, active_flag, updated_by, updated_at FROM trip_cost WHERE active_flag = 1";

            @Cleanup
            PreparedStatement statement = connection.prepareStatement(query);
            @Cleanup
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                TripCostDTO dto = new TripCostDTO();
                dto.setId(resultSet.getInt("id"));
                dto.setCode(resultSet.getString("code"));
                TripDTO tripDTO = new TripDTO();
                tripDTO.setId(resultSet.getInt("trip_id"));
                dto.setTripDTO(tripDTO);
                dto.setAccomodationCost(resultSet.getBigDecimal("accomodation_cost"));
                dto.setTravelCost(resultSet.getBigDecimal("travel_cost"));
                dto.setFoodCost(resultSet.getBigDecimal("food_cost"));
                dto.setActivityCost(resultSet.getBigDecimal("activity_cost"));
                dto.setServiceFees(resultSet.getBigDecimal("service_fees"));
                TaxDTO taxDTO = new TaxDTO();
                taxDTO.setId(resultSet.getInt("tax_id"));
                dto.setTaxDTO(taxDTO);
                dto.setActiveFlag(resultSet.getInt("active_flag"));
                dto.setUpdatedBy(resultSet.getString("updated_by"));
                dto.setUpdatedAt(resultSet.getString("updated_at"));
                list.add(dto);
            }
        } catch (Exception e) {
            LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }

        return list;
    }
}

