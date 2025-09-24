<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>User Registration</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <style>
      .register-container {
        max-width: 900px;
        margin: 40px auto 0 auto;
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: 50vh;
      }
      .register-card {
        display: flex;
        width: 100%;
        border-radius: 18px;
        box-shadow: 0 4px 24px rgba(0,0,0,0.08);
        background: #fff;
        overflow: hidden;
        min-height: 420px;
        max-height: 600px;
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
        font-size: 1.8rem;
    	margin-bottom: 12px;
      }
      .left-panel p {
         font-size: 1rem;
    	 max-width: 280px;
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
      .input-row {
        display: flex;
        gap: 15px;
      }
      .input-row > div {
        flex: 1;
      }
      .mb-3 {
    	margin-bottom: 0.75rem !important;
	  }
      @media (max-width: 991.98px) {
        .register-card {
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
        .input-row {
          flex-direction: column;
        }
      }
      @media (max-width: 576px) {
        .register-container {
          padding: 10px 15px;
        }
      }
    </style>
</head>
<body>
<div class="register-container">
  <div class="register-card">
    <div class="left-panel d-none d-lg-flex">
      
      <svg width="110" height="110" viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img">
        <circle cx="32" cy="32" r="30" fill="#b2e0ff" />
        <path d="M16 44L48 20" stroke="#1976d2" stroke-width="3" stroke-linecap="round"/>
        <path d="M48 44L16 20" stroke="#1976d2" stroke-width="3" stroke-linecap="round"/>
        <path d="M32 12L32 52" stroke="#1976d2" stroke-width="3" stroke-linecap="round"/>
        <circle cx="32" cy="32" r="6" stroke="#1976d2" stroke-width="3"/>
      </svg>
      <h2>THE FIRST AIRWAYS</h2>
      <p>
        Register now to embark on a journey beyond time.<br>
        <strong>Enjoy your travel with divine grace!</strong>
      </p>
    </div>
    <div class="right-panel">
      <h2 class="mb-4 text-center" style="color:#1976d2;">Create Your Account</h2>
      <% 
          String regError = (String) request.getAttribute("regError");
          String regSuccess = (String) request.getAttribute("regSuccess");
          if (regError != null) { 
      %>
          <div class="alert alert-danger"><%= regError %></div>
      <% } else if (regSuccess != null) { %>
          <div class="alert alert-success"><%= regSuccess %></div>
      <% } %>
      <form action="UserRegisterServlet" method="post" autocomplete="off" novalidate 
      				onsubmit="return validateRegistrationForm();">
        <div class="input-row mb-3">
          <div>
            <input type="text" class="form-control" id="fullname" name="fullname" placeholder="Enter Your Full Name"
                value="<%= request.getAttribute("prevFullname") != null ? request.getAttribute("prevFullname") : "" %>"
                required autofocus>
          </div>
        </div>
        <div class="mb-3">
          <input type="email" class="form-control" id="email" name="email" placeholder="Enter Your Email"
              value="<%= request.getAttribute("prevEmail") != null ? request.getAttribute("prevEmail") : "" %>"
              required>
        </div>
        <div class="mb-3">
          <input type="tel" class="form-control" id="mobile" name="mobile"
              placeholder="Your Mobile No"
              value="<%= request.getAttribute("prevMobile") != null ? request.getAttribute("prevMobile") : "" %>"
              required pattern="^\+\d{1,3}\d{10}$">
        </div>
        <div class="mb-3">
          <input type="password" class="form-control" id="password" name="password" placeholder="Create Your Password" required>
          <div class="form-check mt-2">
            <input class="form-check-input" type="checkbox" id="showPassword" onclick="togglePassword()">
            <label class="form-check-label" for="showPassword">Show Password</label>
          </div>
        </div>
        <button type="submit" class="btn btn-primary w-100" style="background:#1976d2;border:none;">Register</button>
        <div class="mt-3 text-center">
          Already have an account? <a href="index.jsp" style="color:#0288d1;">Login here</a>
        </div>
      </form>
      
    </div>
  </div>
</div>
<script>
function togglePassword() {
    var pwd = document.getElementById("password");
    // Uncomment the next line if you add a confirm password field
    // var cpwd = document.getElementById("confirmPassword");
    var type = pwd.type === "password" ? "text" : "password";
    pwd.type = type;
    // if (cpwd) cpwd.type = type;
}
</script>

<script>
function validateRegistrationForm() {
    var name = document.getElementById("fullname").value.trim();
    var email = document.getElementById("email").value.trim();
    var mobile = document.getElementById("mobile").value.trim();
    var password = document.getElementById("password").value.trim();
    var emailRegex = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
    var mobileRegex = /^\+\d{1,3}\d{10}$/;

    if (!name && !email && !mobile && !password) {
        alert("Please fill out all the fields.");
        return false;
    }
    if (!name) {
        alert("Please enter your full name.");
        return false;
    }
    if (!email) {
        alert("Please enter your email address.");
        return false;
    }
    if (!emailRegex.test(email)) {
        alert("Please enter a valid email address.");
        return false;
    }
    if (!mobile) {
        alert("Please enter your mobile number.");
        return false;
    }
    if (!mobileRegex.test(mobile)) {
        alert("Please enter a valid mobile number with country code (e.g., +441234567890).");
        return false;
    }
    if (!password) {
        alert("Please enter your password.");
        return false;
    }
    return true; // All validations passed
}
</script>


</body>
</html>
