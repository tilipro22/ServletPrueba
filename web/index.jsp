<%--
  Created by IntelliJ IDEA.
  User: a757627
  Date: 01/09/2019
  Time: 10:47 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tili.model.Room" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Hostel Servlet</title>

  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@8"></script>

  <script>
    var listBed = [];
    var indexIdBed = 0;

    $(document).ready(function () {
      $("#addRoom").click(function () {
        changeVisibilityById("formRoom");
      });

      $("#viewRoom").click(function () {
          changeVisibilityById("roomList");

          if ($("#roomList").is(":visible")) {
              $("#viewRoom").text("Ocultar Habitaciones");
          }
          else {
              $("#viewRoom").text("Ver Habitaciones");
          }
      });

    if($("#viewRoom").is(":visible")){
        $("#viewRoom").text("Ver Habitaciones");
    } else{
        $("#viewRoom").text("Ocultar Habitaciones");
    }

      $("#inputBeds").click(function () {
        changeVisibilityById("formBed");
      });

      $("#addBed").click(function () {
        listBed.push($("#inputTypeBed").val());
        $("#bodyBedTable").append("<tr id='bedId" + indexIdBed + "'>\n" +
                "      <td>" + $("#inputTypeBed option:selected").text() + "</td>\n" +
                "      <td><button id='" + indexIdBed + "' onclick='deleteBed(this.id)' type=\"button\" class=\"btn btn-danger p-2\">Eliminar</button></td>\n" +
                "      <input type=\"hidden\" name=\"arrayBeds\" value=\"" + $("#inputTypeBed").val() + "\" /> a"  +
                "    </tr>");
        indexIdBed++;
      });
      
      $("#submitRoom").click(function () {
          var isValidate = true;
          var nameRoom = $("#inputName").val();

          if (nameRoom.trim() == "") {
              isValidate = false;
              Swal.fire({
                  type: 'error',
                  title: 'Oops...',
                  text: 'No ingreso el nombre de la habitación'
              })
          }
          else if ($('#bodyBedTable').text().trim() == "") {
              isValidate = false;
              Swal.fire({
                  type: 'error',
                  title: 'Oops...',
                  text: 'No añadio ninguna cama a la habitación'
              })
          }

          if (isValidate) {
              $("#formInsert").submit();
          }
      });

      function changeVisibilityById(id) {
          if ($("#" + id).is(":visible")) {
              $("#" + id).hide();
          }
          else {
              $("#" + id).show();
          }
      }

    })

    function deleteRoom(id) {
        id = id.split("-")[1];
        $(document).ready(function () {
            $.ajax({
                url: "deleteRoom",
                data: {id: id},
                success: function (data) {
                    $("#room-" + id).remove();
                }
            });
        });
    }

    function deleteBed(id) {
        $("#bedId" + id).remove();
    }
  </script>

</head>
<body>
<div class="container text-center">
  <h1>Hostel Servlet</h1>
</div>
<div class="container bg-dark text-center">
  <div class="row">
    <div class="col py-2">
      <div id="addRoom" class="btn btn-success">Agregar Habitación</div>
    </div>
    <div class="col py-2">
      <div id="viewRoom" class="btn btn-primary">Ver Habitaciones</div>
    </div>
  </div>
</div>
<div id="formRoom" class="container text-white bg-dark my-2 p-2 border border-success" style="display: none">
  <form id="formInsert" action="insertRoom" method="post">
    <div class="form-group">
      <label for="inputName">Nombre:</label>
      <input name="nameRoom" type="text" class="form-control" id="inputName" placeholder="Nombre del cuarto">
    </div>
    <div class="form-group">
      <label for="inputTypeRoom">Tipo:</label>
      <select name="typeRoom" id="inputTypeRoom" class="form-control">
        <option value="Compartido">Compartido</option>
        <option value="Privado">Privado</option>
      </select>
    </div>
    <div class="form-group border border-white mb-0 p-2">
      <row class="text-center">
        <div id="inputBeds" class="btn btn-info">Agregar cama</div>
      </row>
    </div>
      <div id="formBed" class="form-group border border-white mt-0 p-2" style="display: none; border-bottom: none">
        <label for="inputTypeBed">Tipo:</label>
        <select id="inputTypeBed" class="form-control">
          <option value="single">Single</option>
          <option value="matrimonial">Matrimonial</option>
          <option value="double">Doble</option>
          <option value="triple">Triple</option>
          <option value="quadruple">Cuadruple</option>
        </select>
        <div class="row m-2 mx-auto">
          <div id="addBed" class="btn btn-secondary mx-auto">Añadir</div>
        </div>
        <table class="table table-dark">
          <thead>
          <tr>
            <th scope="col">Tipo de Cama</th>
            <th scope="col">Acciones</th>
          </tr>
          </thead>
          <tbody id="bodyBedTable">

          </tbody>
        </table>
      </div>
    <div class="form-group pt-3">
      <div class="row mx-auto">
        <div id="submitRoom" class="btn btn-primary mx-auto" style="cursor: pointer"> Listo!</div>
      </div>
    </div>
  </form>
</div>
<div id="roomList" class="container m-2 my-5 mx-auto text-center" <c:if test="${roomList != null}"> <% if ( ((List)request.getAttribute("roomList")).size() == 0){%>style="display: none"<%}%> </c:if> >
    <h1>Lista de Habitaciones</h1>
    <c:if test="${roomList == null}">
    <h6>No hay habitaciones registradas</h6>
    </c:if>
    <div class="row my-1">
    <c:forEach var="room" items="${roomList}">

        <div id="room-${room.id}" class="col-sm-4 px-2">
            <c:if test="${room.occupied == room.capacity}">
                <div class="card text-white bg-warning mb-3" style="max-width: 18rem;">
            </c:if>
            <c:if test="${room.occupied != room.capacity}">
                <div class="card text-white bg-success mb-3" style="max-width: 18rem;">
            </c:if>
                    <div class="card-header"><c:out value="${room.id}"/>. <c:out value="${room.type}"/></div>
                    <div class="card-body">
                        <h5 class="card-title"><c:out value="${room.name}"/></h5>
                        <p class="card-text">Capacidad: ${room.occupied}/<c:out value="${room.capacity}"/></p>
                    </div>
                    <div class="card-footer bg-transparent">
                        <div class="btn btn-primary m-1"><a class="text-white" href="editRoom?id=<c:out value="${room.id}"/>">Editar</a> </div>
                        <div id="delete-${room.id}" class="btn btn-danger m-1" onclick="deleteRoom(this.id)">Eliminar</div>
                    </div>
                </div>
        </div>
    </c:forEach>
    </div>

</div>
</body>
</html>

