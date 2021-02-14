<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ajouter une catégorie</title>

<!-- Bootstrap core CSS -->
<link href="../vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="../css/customstylesheet.css" rel="stylesheet">

</head>

<body class="body-flex">

	<%@ include file="../jspf/header.jspf"%>

	<div class="content">

		<div class="container my-4 pt-5">

			<h2 class="my-4">Ajouter une catégorie</h2>
			<hr>

			<c:if test="${not empty requestScope.succesAjoutCategorie}">
				<!-- attribut envoyé par la servlet AjouterCategorie -->
				<div class="alert alert-success" role="alert">${succesAjoutCategorie}</div>
			</c:if>

			<c:forEach var="couple" items="${mapErreurs}">
				<div class="alert alert-danger" role="alert">${couple.value}</div>
			</c:forEach>

			<form action="<%=request.getContextPath()%>/admin/ajouter-categorie" method="POST">

				<div class="row">
					<div class="col">
						<label for="libelle_categorie" class="form-label">Libellé : </label>
						<input id="libelle_categorie" type="text" value="${param.libelle_categorie}" name="libelle_categorie" class="form-control" required />
					</div>
				</div>

				<div class="row mt-3">
					<div class="mb-3 col-sm-12 col-xs-12">
						<button class="btn btn-success">Ajouter</button>
					</div>
				</div>

			</form>
		</div>
	</div>
</body>
</html>