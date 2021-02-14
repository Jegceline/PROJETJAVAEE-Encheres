<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modifier une catégorie</title>

<!-- Bootstrap core CSS -->
<link href="../vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="../css/customstylesheet.css" rel="stylesheet">

</head>

<body class="body-flex">

	<%@ include file="../jspf/header.jspf"%>

	<div class="content">

		<div class="container my-4 pt-5">

			<h2 class="my-4">Modifier une catégorie</h2>
			<hr>

			<c:if test="${empty param.noCategorie}"> 
			<!-- attribut envoyé par la servlet ModifierArticle -->

			<form action="<%=request.getContextPath()%>/admin/modifier-categorie" method="POST">

				<div class="row">
					<div class="col-sm">
						<%@ include file="../jspf/liste-categories.jspf"%>
					</div>
				</div>

				<div class="mb-3 col-sm-12 col-xs-12">
					<button class="btn btn-success" name="modifier">Modifier</button>
				</div>
			</form>
			
			</c:if>
			
			<c:if test="${not empty param.noCategorie}"> 
			<!-- attribut envoyé par la servlet ModifierArticle -->
			
			<form action="<%=request.getContextPath()%>/admin/modifier-categorie" method="POST">

				<div class="row mb-4">
					<div class="col-sm">
						<label for="libelle" class="form-label my-2">Libellé : </label>
						<input id="libelle" type="text" value="${ param.nom_article } ${article.nomArticle}" name="nom_article" class="form-control" required 
						<c:if test="${not empty venteEnCours}">disabled</c:if> /> 
					</div>
				</div>

				<div class="mb-3 col-sm-12 col-xs-12">
					<button class="btn btn-success" name="enregistrer">Enregistrer</button>
				</div>
			</form>
			
			</c:if>
			
		</div>
	</div>
</body>
</html>