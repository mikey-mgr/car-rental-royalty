<template>
    <footer>
        <div class="container pt-5 px-3 w-100">
            <div class="row justify-content-center">
                <div class="col-md-4 col-9">
                    <ul class="list-unstyled">
                    <li class="mb-2">
                        <a href="#" class="footer-link">
                            <router-link class="navbar-brand" :to="{ name: 'HomeView' }" >
                                <img id="logo" src="../../public/logo-dark.jpg"/>
                            </router-link>
                        </a>
                    </li>
                    <li class="text-light fw-bold pb-2">Follow us on our Social platforms</li>
                    <li class="row justify-content-start">
                        <div class="row">
                            <div class="footer-link col text-start"><a href="https://instagram.com/" class="footer-link media-icon bi bi-instagram"></a></div>
                            <div class="footer-link col text-start"><a href="https://linkedin.com/" class="footer-link media-icon bi bi-linkedin"></a></div>
                            <div class="footer-link col text-start"><a href="https://twitter.com/" class="footer-link media-icon bi bi-twitter"></a></div>
                            <div class="footer-link col text-start"><a href="https://facebook.com/" class="footer-link media-icon bi bi-facebook"></a></div>
                            <div class="footer-link col text-start"><a href="https://youtube.com/" class="footer-link media-icon bi bi-youtube"></a></div>
                        </div>
                    </li>
                    </ul>
                </div>
                <div class="col-md-2 col-9">
                    <ul  class="list-unstyled">
                    <li class="text-light fw-bold pb-2">Get to know us</li>
                    <li><router-link class="text-decoration-none footer-link" :to="{name: 'AboutUs'}">About</router-link></li>
                    <li><a href="#" class="footer-link">IOS App</a></li>
                    <li><a href="#" class="footer-link">Learn More</a></li>
                    </ul>
                </div>
                <div class="col-md-2 col-9">
                    <ul  class="list-unstyled">
                    <li class="text-light fw-bold pb-2">Quick Links</li>
                    <li><router-link class="text-decoration-none footer-link" :to="{name: 'HomeView'}">Home</router-link></li>
                    <li><router-link class="text-decoration-none footer-link" :to="{name: 'VehiclesView'}">Vehicles</router-link></li>
                    <li><a href="#" class="footer-link">FAQ</a></li>
                    <li><a href="#" class="footer-link">Service</a></li>
                    </ul>
                </div>
                <div class="col-md-2 col-9">
                    <ul  class="list-unstyled">
                    <li class="text-light fw-bold pb-2">Legal</li>
                    <li><a href="#" class="footer-link">Terms & Conditions</a></li>
                    <li><a href="#" class="footer-link">Privacy Policy</a></li>
                    <li><a href="#" class="footer-link">Feedback</a></li>
                    <li><a href="#" class="footer-link">Cookie Policy</a></li>
                    </ul>
                </div>
                <div class="col-md-2 col-9">
                    <ul  class="list-unstyled">
                        <li><router-link class="text-decoration-none footer-link" :to="{name: 'ContactUs'}">Contact Us</router-link></li>
                        <li><address class="text-light">36912 The Chase<br>
                                    Mt. Pleasant, Harare<br>
                                    <a href="#" class="footer-link">0788667111</a>
                            </address>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </footer>
</template>

<script>
export default {
    name: "FooterView",
}
</script>

<style scoped>

footer{
    /* margin above footer is transparent and showed the fixed hero; use padding so the band is painted */
    margin-top: 0;
    padding-top: 40px;
    position: relative;
    z-index: 40;
    isolation: isolate;
    background-color: var(--navbar-bg) !important;
    font-size: 16px;
    transition: background-color 0.3s ease;
}
.footer-link{
    text-decoration: none;
    color: var(--text-primary);
    transition: color 0.2s ease;
}
.media-icon{
    color: #334155; /* match footer text by default */
    font-size: larger;
    transition: color 0.15s ease, transform 0.15s ease;
}
.footer-link:hover{
    color: var(--accent-color) !important; transform: translateY(-2px);
}

/* Brand base colors for icons */
.media-icon.bi-instagram { color: #E1306C; }
.media-icon.bi-linkedin { color: #0A66C2; }
.media-icon.bi-twitter { color: #1DA1F2; }
.media-icon.bi-facebook { color: #1877F2; }
.media-icon.bi-youtube { color: #FF0000; }

/* On hover, highlight to accent color for emphasis */
.media-icon:hover { color: var(--accent-color) !important; transform: translateY(-2px); }
li{
    padding-bottom: 10px;
}
ul{
    padding: 0px;
}

/* Override Bootstrap text-light class in footer for theme support */
footer .text-light {
    color: var(--text-primary) !important;
}
#logo {
  width: 95px;
  content: url('../../public/logo-dark.jpg');
  transition: filter 0.3s ease;
}

/* Switch to white logo in light mode */
[data-theme="light"] #logo {
  content: url('../../public/logo-white.jpg');
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
}
@media screen {
    .container{
        padding: 48px 36px 0px
    }
}
</style>