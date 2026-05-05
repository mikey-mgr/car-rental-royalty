<template>
    <div id="home">
        <div id="background-div"></div>
        <div class="contact mb-5">
            <div class="container p-5 text-center contact-us col-sm-10">
                <div class="row">
                    <h1 class="display-6" id="contact-form">Contact Us</h1>
                    <h4 class="mb-5">Do you want to send us a message?<br></h4>
                    <p class="text-start mt-5" style="font-size: large;">If you can't find an answer to your questions,
                        please don't hesitate to contact us. In order for us to give you the best possible help please
                        make sure that you get in touch with the correct person.
                    </p>
                    <form @submit="contactUs" class="row g-3">
                        <div class="col-md-4">
                            <label for="firstName" class="form-label">First name</label>
                            <input v-model="firstName" type="text" class="form-control" placeholder="John" id="firstName" required>
                        </div>
                        <div class="col-md-4">
                            <label for="lastName" class="form-label">Last name</label>
                            <input v-model="lastName" type="text" class="form-control" placeholder="Doe" id="lastName" required>
                        </div>
                        <div class="col-md-4">
                            <label for="email" class="form-label">Email</label>
                            <div class="input-group">
                            <input v-model="email" type="text" class="form-control" placeholder="johnd@gmail.com" id="email" aria-describedby="inputGroupPrepend" required>
                            </div>
                        </div>
                        <div class="col-md-4 col-xs-6">
                            <label for="city" class="form-label">City</label>
                            <input v-model="city" type="text" class="form-control" placeholder="Harare" id="city" required>
                        </div>
                        <div class="col-md-4 col-xs-6">
                            <label for="chooseCountry" class="form-label">Country</label>
                            <select v-model="country" class="form-select" id="chooseCountry" required>
                            <option selected disabled value="">Choose...</option>
                            <option>Zimbabwe</option>
                            <option>South Africa</option>
                            <option>United Arab Emirates</option>
                            <option>Australia</option>
                            <option>Melbourne</option>
                            <option>Angola</option>
                            <option>United Kingdom</option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label for="phone" class="form-label">Phone</label>
                            <input v-model="phone" type="text" class="form-control" placeholder="+263782223334" id="phone" required>
                        </div>
                        <div class="col-12">
                            <label for="message" class="form-label">Your Message</label>
                            <textarea v-model="message" rows="5" class="form-control" id="message" placeholder="Enter Message" style="height: 125px !important" required></textarea>
                        </div>
                        <div class="col-12 mt-4">
                            <button class="btn btn-primary contact-btn col-5" type="submit">Send Message</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="container text-center get-in-touch" style="margin-top: 90px;">
            <h4 class="display-6">Get in touch with us</h4>
            <p>Want to get in touch with us?</p>
            <div class="row justify-content-evenly mt-5">

                <div class="card px-0 my-4" style="width: 18rem;">
                    <div class="container p-3">
                    <img src="../assets/AppImages/call-center.svg" class="card-img-top" alt="call center">
                    </div>
                    <div class="card-body">
                        <div class="container pb-4 px-0">
                        <h5 class="card-title">Contact Center</h5>
                            <p class="card-text">Email:<br><a href="#">office@apexcarrental.com</a></p>
                            <p>Phone: <br>+263776543210</p><br>
                            <p>(available 24/7, sometimes you may be required to wait)</p>
                        </div>
                            <a href="#contact-form" class="btn btn-primary call-btn px-5" style="bottom: 0;">Call now</a>
                    </div>
                </div>

                <div class="card px-0 my-4" style="width: 18rem;">
                    <div class="container p-3">
                    <img src="../assets/AppImages/ceo-office.svg" class="card-img-top" alt="ceo">
                    </div>
                    <div class="card-body">
                        <div class="container px-0">
                        <h5 class="card-title">Ceo Office</h5>
                            <p class="card-text">Email: <br><a href="#">mgr@apexcarrental.com</a></p>
                            <p>Phone: <br>+263788667111 <br>+263789123454</p><br>
                            <p>(available 24/7, sometimes you may be required to wait)</p>
                        </div>
                            <a href="#contact-form" class="btn btn-primary call-btn px-5">Call now</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="container mv-5">
          <div class="container my-5 px-4">
            <div class="text-center pb-4">
                <h2>Branch Offices</h2>
                <p>Please look at the list of branch offices in Africa, Europe, Australia and the Middle East</p>
            </div>
            <div class="row text-start d-flex justify-content-space-between">
              <div class="col-lg-6 col-md-6 col-sm-12 my-3">
                <div class="container-fluid">
                  <h4 class="border-bottom border-secondary pb-2">Africa</h4>
                  <li><strong>Zimbabwe -</strong> Harare, Bulawayo</li>
                  <li><strong>South Africa -</strong> Johannesburg</li>
                  <li><strong>Angola -</strong> Luanda</li>
                </div>
              </div>

              <div class="col-lg-6 col-md-6 col-sm-12 my-3">
                <div class="container-fluid">
                  <h4 class="border-bottom border-secondary pb-2">Europe</h4>
                  <li><strong>United Kingdom -</strong> London</li>
                </div>
              </div>

              <div class="col-lg-6 col-md-6 col-sm-12 my-3">
                <div class="container-fluid">
                  <h4 class="border-bottom border-secondary pb-2">Middle East</h4>
                  <li><strong>United Arab Emirates -</strong> Dubai</li>
                </div>
              </div>

              <div class="col-lg-6 col-md-6 col-sm-12 my-3">
                <div class="container-fluid">
                  <h4 class="border-bottom border-secondary pb-2">Australia</h4>
                  <li><strong>Melbourne</strong></li>
                  </div>
              </div>
            </div>
          </div>
        </div>
    </div>
    <div class="container mt-5 px-5">
        <h3>Visit us</h3>
        <a class="text-decoration-none text-dark" href="https://maps.app.goo.gl/SU6bUuAfL1exDXXH8">
        </a>
    </div>
    <div class="container mt-4 px-5" style="height: 60vh">
        <iframe src="https://www.google.com/maps/embed?pb=!1m17!1m12!1m3!1d3799.4174172123485!2d31.049516075055923!3d-17.77206898318216!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m2!1m1!2zMTfCsDQ2JzE5LjUiUyAzMcKwMDMnMDcuNSJF!5e0!3m2!1sen!2szw!4v1777922138615!5m2!1sen!2szw" width="100%" height="100%" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
    </div>
</template>

<script>
import axios from 'axios';
import swal from 'sweetalert';

export default {
    name: 'ContactUs',
    props: ["baseURL"],
    data() {
        return {
            firstName: null,
            lastName: null,
            email: null,
            city: null,
            country: null,
            phone: null,
            message: null
        }
    },

    methods: {
        async contactUs(e){
            e.preventDefault();
            const message = {
                name: this.firstName + " " + this.lastName,
                email: this.email,
                city: this.city,
                country: this.country,
                phone: this.phone,
                message: this.message,
            }

            await axios.post(`${this.baseURL}/contact/submit`, message, {withCredentials: true})
            .then((res) => {
                if(res.data.message != "Message has been sent"){
                    swal({
                        text: "Message not sent",
                        icon: "error"
                    })
                } else {
                    swal({
                        text: "Message has been sent! We will respond to you shortly",
                        icon: "success"
                    });
                    window.location.replace("/home");
                }
            }).catch((err) => console.log('err', err));
        }
    },
}
</script>

<style scoped>
#background-div {
  margin-top: -55px;
  margin-bottom: -20px;
  background: url("../assets/AppImages/cars/keys.jpg");
  background-size: cover !important;
  min-height: 90vh;
}
.contact-us{
  margin-top: -180px;
  margin-bottom: 2rem;
  background-color: var(--bg-card);
  transition: all 0.3s ease;
}
.card-img-top{
    width: 5.8rem;
    height: 5.8rem;
}
.card {
  background-color: var(--bg-card) !important;
  color: var(--text-primary) !important;
}
.contact-btn{
    border-radius: 0;
}
.contact-btn:hover{
    transform: translateY(-2px);
}
.call-btn{
    border-radius: 0;
}
@media (min-width: 0px) and (max-width: 992px) {
    div.contact-us{
      margin-top: -1rem;
    }
  }
  @media (min-width: 0px) and (max-width: 500px) {
    #background-div{
        display: none;
    }
  }
  div{
    transition: all 0.3s ease;
  }
  
h3, h4, h5, p {
  color: var(--text-primary);
}
</style>