package com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth;

/*
    Abstraction for user signUp/login authentication classes
 */
public abstract class Authentication {

    protected RegisterUserAuthenticationCallbacks mRegisterUserAuthenticationCallbacks;
    protected AuthenticationCallbacks mAuthenticationCallbacks;

    /*
    Constructor for when user is expected to have been already logged in
    this constructor is needed only to call "signOut()" method
     */
    public Authentication() {
    }

    /*
    Constructor for when user is expected to be trying to sign up with a new account
     */
    public Authentication(RegisterUserAuthenticationCallbacks mRegisterUserAuthenticationCallbacks) {
        this.mRegisterUserAuthenticationCallbacks = mRegisterUserAuthenticationCallbacks;
    }

    /*
    Constructor for when user is expected to be trying to log in
    or to authenticate if user is logged in
     */
    public Authentication(AuthenticationCallbacks mAuthenticationCallbacks) {
        this.mAuthenticationCallbacks = mAuthenticationCallbacks;
    }

    public abstract void registerUserAuthentication();
    public abstract void authenticateUser();

    public abstract void signOut();

    public void setmRegisterUserAuthenticationCallbacks(RegisterUserAuthenticationCallbacks mRegisterUserAuthenticationCallbacks) {
        this.mRegisterUserAuthenticationCallbacks = mRegisterUserAuthenticationCallbacks;
    }

    public void setmAuthenticationCallbacks(AuthenticationCallbacks mAuthenticationCallbacks) {
        this.mAuthenticationCallbacks = mAuthenticationCallbacks;
    }

    /*
            callbacks for register new user events
         */
    public interface RegisterUserAuthenticationCallbacks{

        void onRegistrationSuccess(AuthenticationUser user);
        void onRegistrationFailure(String message);
    }

    /*
        callbacks for existing user authentication events
     */
    public interface AuthenticationCallbacks{

        void onAuthenticationSuccess(AuthenticationUser user);
        void onAuthenticationFailure(String message);
    }
}
