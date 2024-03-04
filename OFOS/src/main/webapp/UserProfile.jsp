<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<% 
if (session.getAttribute("email") == null) {
    response.sendRedirect("Login.jsp");
}
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
    <link rel="stylesheet" href="css/UprofileStyle.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container light-style flex-grow-1 container-p-y">
        <h4 class="font-weight-bold py-3 mb-4">Account settings</h4>
        <div class="row">
            <div class="col-md-3 pt-0">
                <div class="list-group list-group-flush account-settings-links">
                    <a class="list-group-item list-group-item-action active" data-toggle="list" href="#account-general">General</a>
                    <a class="list-group-item list-group-item-action" data-toggle="list" href="#account-change-password">Change Password</a>
                    <a class="list-group-item list-group-item-action" data-toggle="list" href="#account-delete-account">Delete Account</a>
                </div>
            </div>
            <div class="col-md-9">
                <div class="tab-content">
                    <div class="tab-pane fade active show" id="account-general">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">My Profile</h5>
                                <form method="post" action="Update">
                                <div class="form-group">
                                    <label class="form-label">Name</label>
                                    <input type="text" class="form-control mb-1" name="name" value="<%= session.getAttribute("name") %>">
                                </div>
                                <div class="form-group">
                                    <label class="form-label">E-mail</label>
                                    <input type="text" class="form-control mb-1" name="email" value="<%= session.getAttribute("email") %>">
                                    <div class="alert alert-warning mt-3">
                                        Your email is not confirmed. Please check your inbox.<br>
                                        <a href="javascript:void(0)">Resend confirmation</a>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">Phone Number</label>
                                    <input type="text" class="form-control" name="phone_number" value="<%= session.getAttribute("phone_number") %>">
                                </div>
                                
                                    <div class="text-right mt-3">
                                        <button type="submit" class="btn btn-primary" name="updateProfile">Update</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="tab-pane fade" id="account-change-password">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">Change Password</h5>
                                <form method="post" action="ChangePassword">
                                <div class="form-group">
                                    <label class="form-label">Current password</label>
                                    <input type="password" class="form-control" name = "current_password">
                                </div>
                                <div class="form-group">
                                    <label class="form-label">New password</label>
                                    <input type="password" class="form-control" name = "new_password">
                                </div>
                                <div class="form-group">
                                    <label class="form-label">Repeat new password</label>
                                    <input type="password" class="form-control" name = "repeat_new_password">
                                </div>
                                <div class="text-right mt-3">
                                    <button type="submit" class="btn btn-primary" >Save changes</button>
                                </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="tab-pane fade" id="account-delete-account">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">Delete Account</h5>
                                <p>Are you sure you want to delete your account? This action cannot be undone.</p>
                                <form method="post" action="DeleteAccount">
                                    <div class="text-right mt-3">
                                        <button type="submit" class="btn btn-danger" name="deleteAccount">Delete Account</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="text-right mt-3">
            <form method="post" action="Logout">
                <button type="submit" class="btn btn-danger">Logout</button>
            </form>
        </div>
    </div>
    <script data-cfasync="false" src="/cdn-cgi/scripts/5c5dd728/cloudflare-static/email-decode.min.js"></script>
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript"></script>
</body>
</html>
