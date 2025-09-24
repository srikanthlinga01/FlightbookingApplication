<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.User.Model.GetUser" %>
<%
    GetUser user = (GetUser) session.getAttribute("loginsuccess");
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile - FlightBooking</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .profile-container {
            max-width: 540px;
            margin: 60px auto;
        }
        .profile-card {
            border-radius: 18px;
            box-shadow: 0 4px 24px rgba(0,0,0,0.08);
        }
        .profile-header {
            background: linear-gradient(135deg, #e0f7fa 0%, #b2ebf2 100%);
            border-radius: 18px 18px 0 0;
            padding: 32px 24px 24px 24px;
            text-align: center;
        }
        .profile-avatar {
            width: 90px;
            height: 90px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid #fff;
            margin-top: -60px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }
        .profile-name {
            font-size: 1.5rem;
            font-weight: 600;
            color: #1976d2;
            margin-top: 12px;
        }
        .profile-details strong {
            color: #1976d2;
        }
        .profile-actions .btn {
            font-weight: bold;
            min-width: 120px;
        }
        .profile-actions .btn + .btn {
            margin-left: 16px;
        }
        .form-label { font-weight: 500; }
        @media (max-width: 576px) {
            .profile-container { padding: 0 8px; }
            .profile-header { padding: 24px 8px 16px 8px; }
        }
    </style>
    <script>
        function submitForm(fieldName) {
            document.getElementById("field").value = fieldName;
            document.getElementById("updateForm").submit();
        }
    </script>
</head>
<body>

<div class="profile-container">
    <div class="card profile-card">
        <div class="profile-header">
            <!-- Placeholder avatar; replace src with user's avatar if available -->
            <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-profiles/avatar-1.webp" alt="Avatar" class="profile-avatar shadow">
            <div class="profile-name"><%= user.getFullname() %></div>
            <div class="profile-details mt-2">
                <div><strong>Email:</strong> <%= user.getEmail() %></div>
                <div><strong>Mobile:</strong> <%= user.getMobile() %></div>
            </div>
        </div>
        <div class="card-body">
            <h5 class="mb-3 text-center text-primary">Edit Your Details</h5>
            <form id="updateForm" action="UpdateUserServlet" method="post" class="mb-4">
                <input type="hidden" id="field" name="field" value="" />
                <div class="mb-3 row align-items-center">
                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="fullname" placeholder="Update Your Name" required>
                    </div>
                    <div class="col-sm-5">
                        <button type="button" class="btn btn-outline-primary w-100 fw-bold" onclick="submitForm('fullname')">Update Name</button>
                    </div>
                </div>
                <div class="mb-3 row align-items-center">
                    <div class="col-sm-7">
                        <input type="email" class="form-control" name="email" placeholder="Update Email" required>
                    </div>
                    <div class="col-sm-5">
                        <button type="button" class="btn btn-outline-primary w-100 fw-bold" onclick="submitForm('email')">Update Email</button>
                    </div>
                </div>
                <div class="mb-3 row align-items-center">
                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="mobile" placeholder="Update Mobile Number" required>
                    </div>
                    <div class="col-sm-5">
                        <button type="button" class="btn btn-outline-primary w-100 fw-bold" onclick="submitForm('mobile')">Update Mobile</button>
                    </div>
                </div>
            </form>
            <div class="d-flex justify-content-center gap-3 mt-4">
    			
    			<form action="MyBookingsServlet" method="get" class="m-0">
        			<button type="submit" class="btn btn-primary fw-bold px-4 flex-fill">My Bookings</button>
    			</form>
    				<a href="FlightSearchServlet" class="btn btn-primary fw-bold px-4 flex-fill">Home</a>
    			<form action="DeleteUserServlet" method="post" class="m-0"
          			onsubmit="return confirm('Are you sure you want to delete your profile?');">
        				<input type="hidden" name="email" value="<%= user.getEmail() %>"/>
        				<button type="submit" class="btn btn-primary fw-bold px-4 flex-fill">Delete Profile</button>
    			</form>
			</div>            
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
