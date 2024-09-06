// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getAuth, signInWithEmailAndPassword, createUserWithEmailAndPassword } from "firebase/auth";

// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyAakqke-pi1n4w32N3scRr-ZCz4eLjCvsc",
    authDomain: "ezybd-b8802.firebaseapp.com",
    projectId: "ezybd-b8802", 
    storageBucket: "ezybd-b8802.appspot.com",
    messagingSenderId: "400433254765",
    appId: "1:400433254765:web:9c605147fbc8ea7c96ca64",
    measurementId: "G-1Q1S93LY1D"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

// Initialize Firebase Authentication and get a reference to the service
const auth = getAuth();

// Funcionalidade de Login
document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault(); // Previne o recarregamento da página
   
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    signInWithEmailAndPassword(auth, email, password)
        .then((userCredential) => {
            const user = userCredential.user;
            document.getElementById('message').innerText = `Bem-vindo, ${user.email}!`;
        })
        .catch((error) => {
            document.getElementById('message').innerText = `Erro: ${error.message}`;
        });
});

// Funcionalidade de Sign Up
document.getElementById('signupForm').addEventListener('submit', function(e) {
    e.preventDefault(); // Previne o recarregamento da página
   
    const email = document.getElementById('signupEmail').value;
    const password = document.getElementById('signupPassword').value;

    createUserWithEmailAndPassword(auth, email, password)
        .then((userCredential) => {
            const user = userCredential.user;
            document.getElementById('message').innerText = `Usuário ${user.email} criado com sucesso!`;
        })
        .catch((error) => {
            document.getElementById('message').innerText = `Erro: ${error.message}`;
        });
});
