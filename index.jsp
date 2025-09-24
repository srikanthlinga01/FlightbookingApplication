<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login Here</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .login-container {
            max-width: 900px;
            margin: 40px auto 0 auto;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 60vh;
        }
        .login-card {
            display: flex;
            width: 100%;
            border-radius: 18px;
            box-shadow: 0 4px 24px rgba(0,0,0,0.08);
            background: #fff;
            overflow: hidden;
            min-height: 350px;
        }
        .left-panel {
            flex: 1 1 50%;
            padding: 30px 30px;
            background: linear-gradient(135deg, #e0f7fa 0%, #b2ebf2 100%);
            color: #1976d2;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            text-align: center;
            border-radius: 18px 0 0 18px;
        }
        .left-panel svg {
            margin-bottom: 28px;
        }
        .left-panel h2 {
            font-size: 1.6rem;
            margin-bottom: 10px;
        }
        .left-panel p {
            font-size: 1rem;
            max-width: 260px;
        }
        .right-panel {
            flex: 1 1 50%;
            padding: 30px 30px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            background: #fff;
        }
        .form-label {
            font-weight: 500;
        }
        .btn-primary {
            font-weight: 600;
        }
        .form-control {
            padding-left: 12px;
            padding-right: 12px;
        }
        .mb-3 {
            margin-bottom: 0.75rem !important;
        }
        @media (max-width: 991.98px) {
            .login-card {
                flex-direction: column;
                min-height: unset;
            }
            .left-panel, .right-panel {
                padding: 30px 20px;
                border-radius: 18px;
            }
            .left-panel {
                border-radius: 18px 18px 0 0;
            }
            .right-panel {
                border-radius: 0 0 18px 18px;
            }
        }
        @media (max-width: 576px) {
            .login-container {
                padding: 10px 15px;
            }
            .left-panel {
                display: none !important;
            }
            .right-panel {
                border-radius: 18px;
            }
        }
    </style>
</head>
<body>
<%
    response.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
%>
<div class="login-container">
  <div class="login-card">
    <div class="left-panel d-none d-lg-flex">
      <svg width="90" height="90" viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img">
        <circle cx="32" cy="32" r="30" fill="#b2e0ff" />
        <path d="M16 44L48 20" stroke="#1976d2" stroke-width="3" stroke-linecap="round"/>
        <path d="M48 44L16 20" stroke="#1976d2" stroke-width="3" stroke-linecap="round"/>
        <path d="M32 12L32 52" stroke="#1976d2" stroke-width="3" stroke-linecap="round"/>
        <circle cx="32" cy="32" r="6" stroke="#1976d2" stroke-width="3"/>
      </svg>
      <h2>THE FIRST AIRWAYS</h2>
      <p>
        Welcome to our services<br>
        <strong>Fly higher, travel farther!</strong>
      </p>
    </div>
    <div class="right-panel">
      <h2 class="mb-4 text-center" style="color:#1976d2;">Login</h2>
      <%-- Show login error or info message if present --%>
      <%
        String loginError = (String) request.getAttribute("loginError");
        if (loginError != null) {
      %>
        <div class="alert alert-danger" role="alert"><%= loginError %></div>
      <%
        }
        String infoMessage = (String) request.getAttribute("infoMessage");
        if (infoMessage != null) {
      %>
        <div class="alert alert-info" role="alert"><%= infoMessage %></div>
      <%
        }
      %>
      <form action="login" method="post" autocomplete="off" novalidate onsubmit="return validateLoginForm();">
        <div class="mb-3">
          <input type="email" class="form-control" id="email" name="email" placeholder="Your Email Here" required autofocus>
        </div>
        <div class="mb-3">
          <input type="password" class="form-control" id="password" name="password" placeholder="Your Password" required>
        </div>
        <button type="submit" class="btn btn-primary w-100">Login</button>
        <div class="mt-3 text-center">
          Not Created An Account? <a href="UserRegistration.jsp">Register Here</a>
        </div>
      </form>
    </div>
  </div>
</div>
<script>
function validateLoginForm() {
    var email = document.getElementById("email").value.trim();
    var pwd = document.getElementById("password").value.trim();
    var emailRegex = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;

    if (!email && !pwd) {
        alert("Please fill out the fields.");
        return false;
    }
    if (!email) {
        alert("Please enter your username/email.");
        return false;
    }
    if (email.includes("@") && !emailRegex.test(email)) {
        alert("Please enter a valid email address.");
        return false;
    }
    if (!pwd) {
        alert("Please enter your password.");
        return false;
    }
    return true;
}
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
