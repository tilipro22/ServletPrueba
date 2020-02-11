<%--
  Created by IntelliJ IDEA.
  User: a757627
  Date: 03/09/2019
  Time: 02:14 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tili.model.Room" %>
<%@ page import="com.tili.model.Bed" %>
<%@ page import="com.tili.model.Bed_Single" %>
<%@ page import="com.tili.model.Bed_Multiple" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Editar Habitación</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
    <div class="container bg-dark mt-1">

        <blockquote class="blockquote text-center text-white">
            <p class="mb-0">Habitación: <c:out value="${room.name}"/>. </p>
            <footer class="blockquote-footer">Tipo: <c:out value="${room.type}"/>. (Capacidad=<c:out value="${room.capacity}"/>)</footer>
        </blockquote>

        <form id="form-update" action="update" method="post">
            <table class="table table-dark">
                <thead>
                <tr>
                    <th scope="col">#id</th>
                    <th scope="col">Nombre</th>
                    <th scope="col">Capacidad</th>
                    <th scope="col">Lugares</th>
                </tr>
                </thead>
                <tbody>
                <%
                    request.getSession().setAttribute("bedList" , request.getAttribute("beds"));
                    int i = 0;
                %>
                <c:forEach var="bed" items="${beds}">

                    <tr>
                        <th scope="row"><c:out value="${bed.id}"/></th>
                        <td><c:out value="${bed.name}"/></td>
                        <td><c:out value="${bed.capacity}"/></td>
                        <td>
                            <%
                                List<Boolean> list = new ArrayList<>();
                                List<Bed> bedList = (List<Bed>) request.getAttribute("beds");

                                if (bedList.get(i).getCapacity() == 1 ) {
                                    list.add( ((Bed_Single)bedList.get(i)).getOccupied() );
                                } else {
                                    list = ((Bed_Multiple)bedList.get(i)).getListOccupied();
                                }

                            %>
                            <c:if test="${bed.capacity == 1}">
                                <input class="form-check-input" type="checkbox" value="bedArrayOccupied-<c:out value="${bed.id}"/>" id="defaultCheck<c:out value="${bed.id}"/>" <%=list.get(0) ? "checked" : ""%> onclick="onClickCheckBox(this.value)">
                                <input id="bedArrayOccupied-<c:out value="${bed.id}"/>" type="hidden" name="bedOccupied-<c:out value="${bed.id}"/>" value="<%=list.get(0) ? "true" : "false"%>">
                            </c:if>
                            <c:if test="${bed.capacity > 1}">
                                <div class="row">
                                    <% for (int j=0; j < list.size(); j++) {%>
                                    <div class="col">
                                        <input class="form-check-input" type="checkbox" value="bedArrayOccupied-<c:out value="${bed.id}"/>-<%=j%>" <%=list.get(j) ? "checked" : ""%> onclick="onClickCheckBox(this.value)">
                                        <input id="bedArrayOccupied-<c:out value="${bed.id}"/>-<%=j%>" type="hidden" name="bedArrayOccupied-<c:out value="${bed.id}"/>" value="<%=list.get(j) ? "true" : "false"%>">
                                    </div>
                                    <%}%>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    <%
                        i++;
                    %>
                </c:forEach>
                </tbody>
            </table>
        </form>

    </div>
    <div class="container text-center">
            <div class="row">
                <div class="col-6 text-right">
                    <div id="btn-guardar" class="btn btn-success"><a class="text-white">Guardar</a></div>
                </div>
                <div class="col-6 text-left">
                    <div class="btn btn-warning"><a class="text-white" href="cancel">Cancelar</a></div>
                </div>
            </div>
    </div>
</body>
<script>
    $(document).ready(function () {
        $("#btn-guardar").click(function () {
            $("#form-update").submit();
        })
    });

    function onClickCheckBox(id) {
        var currentVal = $("#"+id).val();

        if (currentVal == "true") {
            $("#"+id).val("false");
        }
        else {
            $("#"+id).val("true");
        }
    }


</script>
</html>
