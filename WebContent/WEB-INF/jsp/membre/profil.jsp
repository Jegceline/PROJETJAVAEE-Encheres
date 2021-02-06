<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html class="html-100">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Mes informations personnelles</title>

<!-- Bootstrap core CSS -->
<link href="../vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="../css/customstylesheet.css" rel="stylesheet">

</head>

<body class="body-flex">

	<%@ include file="../jspf/header.jspf"%>

	<div class="content">

		<div class="container my-4 pt-5">

			<h2 class="mt-4">Mon profil</h2>

			<hr>

			<!-- Messages conditionnels  -->

			<c:if test="${not empty param.succesModifProfil}">
				<!-- attribut envoyé par la servlet Modifier profil -->
				<div class="alert alert-success" role="alert">${param.succesModifProfil}</div>
			</c:if>

			<!-- fin des messages conditionnels -->

			<form action="<%=request.getContextPath()%>/membre/profil" method="POST">

				<div class="row my-2">
					<div class="col">
						<label for="pseudo" class="form-label">Pseudo : </label> <input id="pseudo" type="text" value="${ profilUtilisateur.pseudo }" name="pseudo"
							class="form-control" disabled />
					</div>

					<div class="col">
						<label for="nom" class="form-label">Nom : </label> <input id="nom" type="text" value="${ profilUtilisateur.nom }" name="nom"
							class="form-control" disabled />
					</div>
				</div>

				<div class="row my-2">
					<div class="col">
						<label for="prenom" class="form-label">Prénom : </label> <input id="prenom" type="text" value="${ profilUtilisateur.prenom }" name="prenom"
							class="form-control" disabled />
					</div>

					<div class="col">
						<label for="email" class="form-label">E-mail : </label> <input id="email" type="text" value="${ profilUtilisateur.email }" name="email"
							class="form-control" disabled />
					</div>
				</div>

				<div class="row my-2">
					<div class="col">
						<label for="telephone" class="form-label">Téléphone : </label> <input id="telephone" type="text" name="telephone"
							value="${ profilUtilisateur.telephone }" class="form-control" disabled />
					</div>

					<div class="col">
						<label for="rue" class="form-label">Rue : </label> <input id="rue" type="text" name="rue" value="${ profilUtilisateur.rue }"
							class="form-control" disabled />
					</div>
				</div>

				<div class="row my-2">
					<div class="col">
						<label for="code_postal" class="form-label">Code postal :</label> <input id="code_postal" type="text" name="codePostal"
							value="${ profilUtilisateur.codePostal }" class="form-control" disabled />
					</div>

					<div class="col">
						<label for="telephone" class="form-label">Ville : </label> <input id="ville" type="text" name="ville" value="${ profilUtilisateur.ville }"
							class="form-control" disabled />
					</div>
				</div>

				<div class="row my-2">
					<div class="col">
						<label for="credit" class="form-label">Crédit :</label> <input id="credit" type="text" name="credit" value="${ profilUtilisateur.credit }"
							class="form-control" disabled />
					</div>

					<div class="col"></div>
				</div>

				<div class="row my-4">
					<div class="col">
						<button class="btn btn-success ">Modifier</button>
					</div>


				</div>

				<!--  
			<p>
				<input type="submit" value="Modifier" />
			</p>
			 -->
			</form>


			<div class="my-4">
				<nav aria-label="breadcrumb">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="<%=request.getContextPath()%>/accueil">Accueil</a></li>
						<li class="breadcrumb-item active" aria-current="page">Profil</li>
					</ol>
				</nav>
			</div>

		</div>
	</div>

	<%@ include file="../jspf/footer.jspf"%>

</body>
</html>