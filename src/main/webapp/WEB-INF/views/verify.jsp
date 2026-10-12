<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Verification failed - Summary App</title>
  <link rel="stylesheet" href="/css/style.css">
</head>
<body>
  <main class="card">
    <h1>Verification failed</h1>
    <c:forEach var="error" items="${errors}">
      <p class="error"><c:out value="${error}"/></p>
    </c:forEach>
    <a class="text-link" href="/test.html">Go back and fix</a>
  </main>
</body>
</html>
