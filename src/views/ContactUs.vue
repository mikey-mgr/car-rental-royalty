<template>
    <div id="home" class="pb-4">
        <div id="background-div"></div>
        <div class="contact mb-5">
            <div class="container p-5 text-center contact-us col-sm-10">
                <div class="row">
                    <h1 class="display-6 ff-bold" id="contact-form">Contact Us</h1>
                    <h4 class="mb-5 ff-light">Do you want to send us a message?<br></h4>
                    <p class="text-start mt-5" style="font-size: large;">If you can't find an answer to your questions,
                        please don't hesitate to contact us. In order for us to give you the best possible help please
                        make sure that you get in touch with the correct person.
                    </p>
                    <form @submit="contactUs" class="row g-3 w-100">
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
                            <button
                               class="btn btn-primary contact-btn"
                              type="submit"
                              :disabled="isSubmitting"
                            >
                              <span
                                v-if="isSubmitting"
                                class="spinner-border spinner-border-sm me-2 button-spinner"
                                role="status"
                                aria-hidden="true"
                              ></span>
                              <span>{{ isSubmitting ? 'Sending...' : 'Send Message' }}</span>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="container text-center get-in-touch px-3 px-lg-3 py-5">
            <h4 class="display-6">Get in touch with us</h4>
            <p>Want to get in touch with us?</p>
            <div class="row justify-content-evenly">

                <div class="card px-0 my-4 d-flex flex-column" style="width: 21rem;">
                    <div class="container p-3">
                    <img src="../assets/AppImages/call-center.svg" class="card-img-top" alt="call center">
                    </div>
                    <div class="card-body d-flex flex-column">
                        <div class="flex-grow-1">
                        <h5 class="card-title">Contact Center</h5>
                            <p class="card-text">Email:<br><a href="#">customercare@deroyaltycarrental.com</a></p>
                            <p>Phone: <br><a href="tel:+263242004351">+263 24 2004351</a></p>
                            <p>Address:<br>2870 Mainway Meadows<br>Waterfalls, Harare</p><br>
                            <p>(available 24/7, sometimes you may be required to wait)</p>
                        </div>
                            <a href="#contact-form" class="btn btn-primary call-btn px-5 mt-auto">Call now</a>
                    </div>
                </div>

                <div class="card px-0 my-4 d-flex flex-column" style="width: 21rem;">
                    <div class="container p-3">
                    <img src="../assets/AppImages/home_page/ceo2Crop.jpg" class="ceo-card-img-top" alt="ceo">
                    </div>
                    <div class="card-body d-flex flex-column">
                        <div class="flex-grow-1">
                        <h5 class="card-title">Ceo Office</h5>
                            <p class="card-text">Email: <br><a href="#">brianlangsoni@deroyaltycarrental.com</a></p>
                            <p>Phone: <br><a href="tel:+263712768037">+263712768037</a></p><br>
                            <p>(available 24/7, sometimes you may be required to wait)</p>
                        </div>
                            <a href="#contact-form" class="btn btn-primary call-btn px-5 mt-auto">Call now</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="container-fluid px-5 px-lg-3">
            <div class="office-hours-card mx-auto p-4 p-md-5">
                <h4 class="display-6 text-center mb-4">Office Hours</h4>
                <div class="hours-row">
                    <span class="day">Mon – Fri</span>
                    <span class="separator"></span>
                    <span class="time">08:00 – 17:00</span>
                </div>
                <div class="hours-row">
                    <span class="day">Saturday</span>
                    <span class="separator"></span>
                    <span class="time">08:00 – 13:00</span>
                </div>
                <div class="hours-row">
                    <span class="day">Sunday</span>
                    <span class="separator"></span>
                    <span class="time closed">Closed</span>
                </div>
            </div>
        </div>

        <div class="container px-5 px-lg-3 py-5">
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
        <div class="container px-5 px-lg-3 py-1">
            <h3>Visit us</h3>
            <address>
                2870 Mainway Meadows<br>
                Waterfalls, Harare<br>
                <a href="tel:+263712768037">+263712768037</a>
            </address>
            <a class="text-decoration-none text-dark" href="https://maps.app.goo.gl/SU6bUuAfL1exDXXH8">
            </a>
        </div>
        <div class="container px-5 px-lg-3" style="height: 60vh">
            <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d14235.753719474422!2d31.041371909903418!3d-17.895257864334262!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x1931a3f22ee38a99%3A0x757348b8f2bdbc19!2sDeRoyalty%20Car%20Rental!5e0!3m2!1sen!2szw!4v1781128963161!5m2!1sen!2szw" width="100%" height="100%" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
        </div>
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
            message: null,
            isSubmitting: false
        }
    },

    methods: {
        async contactUs(e){
            e.preventDefault();

            if (this.isSubmitting) {
                return;
            }

            this.isSubmitting = true;
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
                    this.$router.push({ name: 'HomeView' });
                }
            }).catch(() => {
                swal({
                    text: "Message failed to send. Please try again.",
                    icon: "error"
                });
            }).finally(() => {
                this.isSubmitting = false;
            });
        }
    },
}
</script>

<style scoped>
#background-div {
  margin-top: -55px;
  margin-bottom: -20px;
  background: url("../assets/AppImages/keys.jpg");
  background-size: cover !important;
  min-height: 90vh;
}
.contact-us{
  margin-top: -180px;
  margin-bottom: 2rem;
  background-color: var(--bg-primary) !important;
  transition: all 0.3s ease;
}
.ceo-card-img-top{
    width: 5.8rem;
    height: 5.8rem;
    background-color: white;
    padding: 2px;
    border-radius: 8px;
}
.card-img-top{
    width: 5.8rem;
    height: 5.8rem;
    background-color: white;
    padding: 0.5rem;
    border-radius: 8px;
}

.ff-bold{
  font-family: var(--font-akrobat-bold);
}
.ff-semibold{
  font-family: var(--font-akrobat-semibold);
}
.ff-regular{
  font-family: var(--font-akrobat-regular);
}
.ff-light{
  font-family: var(--font-akrobat-light);
}

.card-body{
    background-color: var(--bg-primary) !important;
}
.card {
  background-color: var(--bg-primary) !important;
  color: var(--text-primary) !important;
}
.contact-btn{
    border-radius: 0;
}
.contact-btn:hover{
    transform: translateY(-2px);
}
.button-spinner {
  color: black;
}
form.row.g-3.w-100{
    margin: auto;
}

.office-hours-card {
  max-width: 400px;
  background: var(--bg-primary);
  border-radius: 12px;
}
.hours-row {
  display: flex;
  align-items: center;
  padding: 0.6rem 0;
  border-bottom: 1px solid var(--border-color, #eee);
}
.hours-row:last-child {
  border-bottom: none;
}
.day {
  font-weight: 600;
  min-width: 100px;
  color: var(--text-primary);
}
.separator {
  flex: 1;
  height: 1px;
  margin: 0 1rem;
  background: linear-gradient(90deg, var(--border-color, #ccc), transparent);
}
.px-5{
    padding-left: 2.2rem !important;
    padding-right: 2.2rem !important;
}

.time {
  font-weight: 500;
  color: var(--accent-color, #c18e32);
  min-width: 110px;
  text-align: right;
}
.time.closed {
  color: #dc3545;
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
