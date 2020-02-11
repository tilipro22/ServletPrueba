package com.tili.web;

import com.tili.dao.HostelDAO;
import com.tili.model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Servlet extends javax.servlet.http.HttpServlet {

    private HostelDAO hostelDAO;

    @Override
    public void init() throws ServletException {
        hostelDAO = new HostelDAO();
    }

    protected void doPost(HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        switchAction(request.getServletPath(), request, response);
    }

    private void switchAction(String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        switch (action) {

            case "/insertRoom":
                insertRoom(request, response);
                break;

            case "/editRoom":
                showEditRoom(request, response);
                break;

            case "/update":
                updateRoom(request, response);
                break;

            case "/deleteRoom":
                deleteRoom(request, response);
                break;

            default:
                listRooms(request, response);
                break;

        }
    }

    private void listRooms(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Room> rooms = hostelDAO.selectAllRooms();
        request.setAttribute("roomList" , rooms);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
        requestDispatcher.forward(request, response);
    }

    private void insertRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("nameRoom");
        String type = request.getParameter("typeRoom");
        String[] arrayBeds = request.getParameterValues("arrayBeds");
        List<Bed> bedList = new ArrayList<>();



        for (String bedType:
                arrayBeds) {
            if (BedEnum.SINGLE.name().equals(bedType.toUpperCase())) {
                bedList.add(new Bed_Single(bedType));
            }
            else {
                bedList.add(new Bed_Multiple(bedType));
            }
        }

        Room room = new Room(name, type, bedList);

        hostelDAO.insertRoom(room);

        for (Bed bed:
             room.getBeds()) {
            hostelDAO.insertBed(bed);
        }

        response.sendRedirect("list");

    }

    private void showEditRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Room room = hostelDAO.selectRoomById(id);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("editRoom.jsp");
        request.setAttribute("room" , room);
        request.setAttribute("beds" , room.getBeds());

        requestDispatcher.forward(request, response);
    }

    private void deleteRoom(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        hostelDAO.deleteRoom(id);
    }

    private void updateRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Bed> beds = (List<Bed>) request.getSession().getAttribute("bedList");

        for (Bed bed:
             beds) {
            Integer id = bed.getId();

            if (bed instanceof Bed_Single) {

                boolean bedOccupied = request.getParameter("bedOccupied-" + id).equals("true") ? true : false;
                hostelDAO.updateOccupiedBedById(id, bedOccupied);
            }
            else {
                List<Boolean> listOccupied = ((Bed_Multiple) bed).getListOccupied();

                for (int i=0; i < listOccupied.size(); i++) {
                    String[] arrayOccupied = request.getParameterValues("bedArrayOccupied-" + id);
                    listOccupied.set(i, arrayOccupied[i].equals("true") ? true : false);
                }

                hostelDAO.updateOccupiedBedById(id, listOccupied);
            }
        }

        response.sendRedirect("list");
    }



}
