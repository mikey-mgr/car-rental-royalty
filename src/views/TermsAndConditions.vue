<template>
  <main id="terms-and-conditions" class="terms-page">
    <section class="terms-hero">
      <div class="container">
        <p class="terms-kicker mb-2">DeRoyalty Car Rental</p>
        <h1 class="display-6 mb-3">Terms and Conditions of Rental Agreement</h1>
        <p class="terms-disclaimer mb-0">Which limit your rights - please read carefully</p>
      </div>
    </section>

    <section class="container terms-content">
      <div class="terms-panel">
        <p class="fw-bold mb-2">Disclaimer</p>
        <p class="mb-4">Copyright DeRoyalty Car Rental</p>

        <article
          v-for="section in termsSections"
          :key="section.title"
          class="terms-section"
        >
          <h2>{{ section.title }}</h2>
          <p
            v-for="clause in section.clauses"
            :key="clause"
          >
            {{ clause }}
          </p>
        </article>

        <article class="terms-section contact-address">
          <h2>Contact address</h2>
          <address>
            2870 Mainway Meadows<br>
            Waterfalls, Harare<br>
            <a href="tel:+263712768037">+263712768037</a>
          </address>
        </article>
      </div>
    </section>
  </main>
</template>

<script>
export default {
  name: 'TermsAndConditions',
  data() {
    return {
      termsSections: [
        {
          title: '1. DEFINITIONS',
          clauses: [
            'In this AGREEMENT, unless the context indicates otherwise, the following expressions will bear the following meanings:',
            '1.1 "DCR", "OUR", "US" and "WE" means DEROYALTY CAR RENTAL (PTY) LTD;',
            '1.2 "the RENTER", "YOU" or "YOUR" means the renter of the VEHICLE, as indicated in the AGREEMENT and who must be 23 years old or older and who must have held a valid unendorsed driver\'s license for a minimum period of 2 years prior to the AGREEMENT;',
            '1.3 "The DRIVER" means YOU and/or the driver and/or the ADDITIONAL DRIVER as indicated in the AGREEMENT and who must be 23 years old or older and who must have held a valid unendorsed driver\'s license for a minimum period of 2 years prior to the AGREEMENT;',
            '1.4 "DAY" means a period of 24 hours (or any part thereof) calculated from the time the vehicle is received by YOU;',
            '1.5 "the VEHICLE" means the VEHICLE/s identified in this AGREEMENT or any other replacement vehicle provided to YOU by US (including the VEHICLE documents, keys, tyres, tools and accessories supplied with the VEHICLE);',
            '1.6 "the RENTAL PERIOD" means the period between the date when the vehicle is taken out by YOU and the termination date and time as specified on the AGREEMENT or, if such period is extended, the time and date entered on OUR records as being the date and time when the VEHICLE is returned to US;',
            '1.7 "The OFFICIAL RATES" means OUR rates charged from time to time and/or in terms of the official rates published and amended from time to time, applicable to the VEHICLE rented in terms of this AGREEMENT, and which are available at any of OUR offices;',
            '1.8 "the LIABILITY WAIVERS" means the waivers referred to in clause 5, including but not limited to, the items identified as CDW, SCDW, TLW and STLW, but which specifically exclude any key replacement and the loss of personal belongings. The LIABILITY WAIVERS are not an insurance policy but provide a basis on which YOUR liability in terms of this AGREEMENT may be reduced.',
            '1.9 "AGREEMENT" means the rental agreement and these terms and conditions.',
            '1.10 References to the singular will include the plural, the male gender will include the female gender, and references to persons will include natural and juristic persons.',
          ],
        },
        {
          title: '2. RISK, DELIVERY AND RETURN',
          clauses: [
            '2.1 The VEHICLE will be at YOUR sole risk from the date and time of delivery of the VEHICLE until the VEHICLE is returned to US. YOU undertake to return the VEHICLE in the same condition that YOU received it, fair wear and tear excepted;',
            '2.2 YOU will return the VEHICLE, on the expiry or termination of this AGREEMENT, at YOUR expense to our authorised representative at the collection address recorded in the AGREEMENT. YOU acknowledge that failure to return the VEHICLE in terms of this AGREEMENT will constitute a breach of the AGREEMENT and illegal possession by YOU, and WE may report the VEHICLE as stolen and/or repossess the VEHICLE wherever same may be found and from whomsoever is in possession thereof;',
            '2.3 When YOU or any person nominated by YOU return the VEHICLE to any branch of BCR, YOU will:',
            '2.3.1 Park the VEHICLE in DCR\'s reserved parking;',
            '2.3.2 Ensure that the VEHICLE is properly locked and secure;',
            '2.3.3 Hand the keys of the VEHICLE to an authorised representative of DCR when DCR\'s offices are open for business;',
            '2.3.4 Leave the keys in a drop safe provided at the offices of DCR when DCR\'s offices are not open for business.',
            '2.4 The sole risk of loss or damage to the VEHICLE will remain vested in YOU until such time as WE have recorded the return of the VEHICLE.',
          ],
        },
        {
          title: '3. WARRANTIES BY YOU',
          clauses: [
            'YOU warrant that:',
            '3.1 All information given by YOU to US is true and correct;',
            '3.2 The DRIVER holds a valid unendorsed DRIVER\'s license for the VEHICLE;',
            '3.3 YOU will not drive the VEHICLE under the influence of alcohol or any other central nervous system stimulant;',
            '3.4 The DRIVER is not physically prevented from operating the VEHICLE safely;',
            '3.5 No person other than the DRIVER will drive the VEHICLE;',
            '3.6 The DRIVER will lock the VEHICLE and activate any burglar alarm or protection system installed in the VEHICLE when same is not in use and ensure that the keys of the VEHICLE are properly controlled;',
            '3.7 the VEHICLE will not be used or driven for the conveyance of persons or property for reward, in contravention of or in breach of any law, in any race, speed test or contest, on roads not properly constructed, or for towing unless supplied with a tow bar;',
            '3.8 The VEHICLE will not be used or driven in any way which would constitute a breach of any of the provisions of this AGREEMENT;',
            '3.9 YOU and the DRIVER will at all times display an absolute duty of care towards BCR in respect of the VEHICLE, and YOU will ensure that the VEHICLE will only be used on suitable roads and conditions in accordance with the type of VEHICLE hereby rented;',
            '3.10 YOU and the DRIVER will not take the VEHICLE into any area or on any road where there is a risk that the VEHICLE may be damaged, stolen or lost through civil disturbance, riot or any act of political unrest;',
            '3.11 The VEHICLE will not be taken outside the Republic of Zimbabwe, except with OUR prior written permission. The cross border letter provided by US will provide YOU with authorisation to cross the relevant border and WE will not be held responsible for any fee/or other costs/payments howsoever incurred by YOU crossing the border.',
          ],
        },
        {
          title: '4. PAYMENTS',
          clauses: [
            '4.1 YOU agree to pay US:',
            '4.1.1 The VEHICLE rental rates, cross border fees per border and other charges as set out in the AGREEMENT and/or in terms of our OFFICIAL RATES, whichever may be applicable;',
            '4.1.2 A refuelling fee and the cost of fuel supplied for the VEHICLE by US which cost will be calculated upon return of the VEHICLE. If the VEHICLE is delivered to, or collected from YOU, YOU will be charged for fuel used from the time it leaves the dispatching branch to the time it is returned to the returning branch. Kindly note WE charge $3/litre if VEHICLE needs fuel top up when brought back with less fuel;',
            '4.1.3 all fines, taxes, charges, levies, legal costs and tolls, including e-tolls, payable by US to any third party arising out of YOUR use of the VEHICLE;',
            '4.1.4 all and any costs, including but not limited to, one-way fees, towing charges and losses or damages incurred by US in procuring the return of the VEHICLE to the collection address described above, or such other location as determined by US;',
            '4.1.5 In the event that the VEHICLE is not returned on the return date, all amounts that would have been payable by YOU in terms of this AGREEMENT if the RENTAL PERIOD had been validly extended to the actual date of return of the VEHICLE to US;',
            '4.1.6 all costs for which YOU are liable, incurred by US in repairing any damage of any nature whatsoever to the VEHICLE and any loss or damages suffered by US as a result of theft, fire or any other cause whatsoever;',
            '4.1.7 Such valet charge as may be levied for the cleaning of the VEHICLE; and',
            '4.1.8 Such accident administration and/or traffic fine handling fee that may be levied by US.',
            '4.2 If WE have agreed to payment from YOU by credit or charge card, YOUR signature will constitute authority for the issuer of the card to debit YOU with the total amount owing, inclusive of all costs and charges of whatsoever nature, arising in terms of this AGREEMENT.',
            '4.3 YOU will pay all amounts payable by YOU under this AGREEMENT to US, on demand. If any payment is not made on its due date, then WE may, without prejudice to any of OUR rights, charge interest on the amount due at the maximum rate permissible by law.',
          ],
        },
        {
          title: '5. LIABILITY WAIVERS',
          clauses: [
            '5.1 YOU may purchase in advance a collision damages waiver (CDW) and/or theft loss waiver (TLW) or a super waiver ("SCDW/STLW"), or such LIABILITY WAIVERS may be included in the OFFICIAL RATES recorded in clause 4.1.1.',
            '5.2 In such event, YOUR liability in terms of clause 2.1 will not exceed the amount stated in the AGREEMENT as the "Renter\'s Responsibility", unless one or more of the exclusions in clause 5.3 is applicable.',
            '5.3 The LIABILITY WAIVERS do not cover loss of, or damage to the VEHICLE in the following circumstances, and YOU will be liable for all such loss or damage:',
            '5.3.1 Where YOU or the DRIVER are in breach of this AGREEMENT;',
            '5.3.2 Where damage is caused to all glass, mirrors, lamps, tyres, rims, hubcaps, windscreens or the undercarriage, if no collision of the VEHICLE has occurred;',
            '5.3.3 Where damage is caused by water;',
            '5.3.4 Where damage or loss is caused by DRIVER negligence;',
            '5.3.5 Where damage or loss is sustained in an accident not caused by physical contact with another vehicle, person, animal or object;',
            '5.3.6 Where damage is caused by YOUR failure to ensure that the VEHICLE\'s required lubricant levels are maintained;',
            '5.3.7 Where damage or loss is caused by pot holes, or on roads not suitable for the type of vehicle rented: and',
            '5.3.8 In respect of personal belongings, key replacement, towing fees and claim administration fees.',
            '5.4 YOU may not decline the LIABILITY WAIVERS offered by US unless:',
            '5.4.1 There is a valid corporate account opened in YOUR name; and',
            '5.4.2 YOU have signed OUR "Self Insurance Agreement"; and',
            '5.4.3 YOU have provided US with written proof from YOUR insurers that all vehicles rented are comprehensively insured.',
            '5.5 Subject to our consent, you may, in addition to the LIABILITY WAIVERS, purchase in advance a hail and/or windscreen and/or tyre waiver. These additional waivers only cover damages caused by hail, damages to viewing glass caused by stone chips, and to tyres, rims and hubcaps caused by potholes and do not cover theft of the said items.',
          ],
        },
        {
          title: '6. EXTENSION OF RENTAL PERIOD',
          clauses: [
            '6.1 YOU will be entitled at any time during the initial period to orally extend the RENTAL PERIOD.',
            '6.2 This extension will however only be valid if confirmed by US in writing.',
            '6.3 YOU have to notify us at least 6 hours before initial time of return of the VEHICLE or otherwise WE will collect the vehicle from you.',
            '6.4 If YOU forfeit payment of the extension within 2 hours of the initial return time WE consider the car stolen. No vehicle is to be USED without full payment.',
          ],
        },
        {
          title: '7. TERMINATION',
          clauses: [
            '7.1 WE will be entitled to terminate this AGREEMENT if YOU and/or the DRIVER commit any breach of this AGREEMENT. WE will then be entitled to the immediate return of the VEHICLE, and furthermore any amount then and there owing by YOU to US will become immediately due and payable.',
            '7.2 Both YOUR and OUR rights and obligations under this AGREEMENT will continue to be in full force and effect until such time as the VEHICLE has been returned to US in terms of this AGREEMENT and YOU have complied with all YOUR obligations in terms hereof.',
          ],
        },
        {
          title: '8. INDEMNITY',
          clauses: [
            '8.1 Save as is provided for in law and provided that there was no negligence on OUR part, WE will not be liable for any damage and/or injury and/or death arising out of any defect in and/or mechanical failure of the VEHICLE, nor for any loss or damages to any property transported in or left in the VEHICLE, nor for any damages, injury, death, consequential loss, loss of profits, or any other damages which the RENTER or the DRIVER or any person transported in the VEHICLE may suffer arising out of this AGREEMENT.',
          ],
        },
        {
          title: '9. RESPONSIBILITY AFTER LOSS OR DAMAGE TO VEHICLE',
          clauses: [
            '9.1 If the VEHICLE is involved in any accident or collision, or is lost or stolen, or is involved in any incident, which could prejudice OUR rights, the RENTER and/or DRIVER will take all such steps to safeguard OUR interests, including, but not limited to the following where appropriate:',
            '9.2 Obtain the names and addresses of everyone involved and of possible witnesses, and details of other vehicles involved;',
            '9.3 Not, without our prior written consent, admit any responsibility or liability or release any party from any liability or potential liability, nor settle any claim or potential claim against or by any party, nor accept any disclaimer of liability;',
            '9.4 Notify the police and US as soon as possible and in any event within 24 hours of the incident;',
            '9.5 Within 48 hours of the incident complete and furnish to US OUR fully completed standard claim form;',
            '9.6 Within 48 hours of the incident submit to US a copy of the DRIVER\'s licence;',
            '9.7 Not abandon the VEHICLE and make adequate provision for its safety and security; and',
            '9.8 Cooperate with US in any investigation, the lodging or instituting of any claim or action and the defence of any prosecution, claim or action relating to the above;',
          ],
        },
        {
          title: '10. GENERAL',
          clauses: [
            '10.1 YOU acknowledge that ownership of the VEHICLE will at all times remain vested in US, or the true owner of the VEHICLE.',
            '10.2 YOU will not be entitled to cede or assign any of YOUR rights and obligations under this AGREEMENT or to sublet or part with possession of the VEHICLE.',
            '10.3 The cost of fuel is not included in the amount of the rental. We will refuel all VEHICLES on return of the VEHICLE to US.',
            '10.4 You are not permitted to carry out any repairs, including replacing any tyres, on the VEHICLE.',
            '10.5 Any tampering by YOU with the odometer of the VEHICLE will be regarded as fraud. Where required in determining the rental charges, the distance driven by YOU or the DRIVER will be measured from the odometer installed in the VEHICLE. If such calculation is not practical, or possible for any reason whatsoever, the calculation will be done by such other reasonable method as WE may determine and YOU will be obliged to furnish all such information and assistance as WE may reasonably require for that purpose.',
            '10.6 If the RENTER is not the DRIVER, then, without in any way detracting from the RENTER\'s obligations in terms of this AGREEMENT, the RENTER and the DRIVER will be liable to US jointly and severally for all and/or any amounts owing under this AGREEMENT.',
            '10.7 Please be advised that although WE will use our best efforts, WE are unable to guarantee the availability of the VEHICLE to YOU at a particular time due to possible circumstances beyond our control. In the event that WE are however unable to do this, WE will endeavour to make alternative arrangements until such time as WE are able to make the VEHICLE available to YOU.',
            '10.8 Save as otherwise stated in this AGREEMENT any addition to or alteration of this AGREEMENT will be null and void unless agreed upon by US in writing.',
            '10.9 YOU agree that WE may sue YOU in the Magistrate\'s Court for all purposes under this AGREEMENT, notwithstanding that the subject matter, cause of action, or amount involved be otherwise beyond the jurisdiction of the said court.',
            '10.10 YOU choose the address where YOU will receive notices for all purposes in terms of this AGREEMENT, at the RENTER\'s address specified in the AGREEMENT or, in the case of that address not being in the Republic of Zimbabwe, at the DRIVER\'s local address specified in the AGREEMENT.',
            '10.11 A certificate of any director, manager or accountant of BCR as to the amount owed by YOU to US will, on the face of it, constitute proof of the amount owing.',
            '10.12 This document contains the entire AGREEMENT between YOU and US regarding the matters contained herein and WE will not be bound by any undertakings, representations, warranties, promises or the like not recorded herein, unless otherwise stipulated by law.',
            '10.13 It is agreed that each clause of these terms and conditions is severable, the one from the other, and if any clause is found to be defective or unenforceable for any reason by any competent Court, then the remaining clauses will be and continue to be of full force and effect.',
            '10.14 This AGREEMENT will be governed by and interpreted in accordance with the laws of the Republic of Zimbabwe.',
            '10.15 No extension, relaxation or other leniency that may be given or allowed by US can be taken to mean that other rights may not be enforced.',
            '10.16 The RENTER and/or DRIVER authorises US to conduct any credit checks which WE deem necessary to adequately assess the risk of contracting with YOU. WE are entitled to note any default by YOU in terms of this AGREEMENT and, in accordance with The National Credit Act\'s requirements, with any credit bureau, and YOU hold US blameless for any consequences arising from the notation.',
          ],
        },
      ],
    };
  },
};
</script>

<style scoped>
.terms-page {
  background-color: var(--page-bg);
  color: var(--text-primary);
}

.terms-hero {
  min-height: 320px;
  display: flex;
  align-items: center;
  background:
    linear-gradient(rgba(10, 20, 40, 0.84), rgba(10, 20, 40, 0.84)),
    url("../assets/AppImages/keys.jpg") center/cover;
}

.terms-kicker {
  color: var(--accent-color);
  font-weight: 700;
  text-transform: uppercase;
}

.terms-disclaimer {
  color: var(--text-secondary);
  font-size: 1.1rem;
}

.terms-content {
  margin-top: -70px;
  padding-bottom: 70px;
}

.terms-panel {
  background-color: var(--bg-card);
  border: 1px solid var(--border-color);
  padding: 2rem;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.18);
}

.terms-section {
  padding-top: 1.5rem;
  border-top: 1px solid var(--border-color);
}

.terms-section:first-of-type {
  border-top: 0;
  padding-top: 0;
}

.terms-section h2 {
  color: var(--accent-color);
  font-size: 1.35rem;
  margin-bottom: 1rem;
}

.terms-section p {
  color: var(--text-primary);
  line-height: 1.7;
  margin-bottom: 0.8rem;
}

.contact-address address {
  color: var(--text-primary);
  line-height: 1.8;
  margin-bottom: 0;
}

.contact-address a {
  color: var(--accent-color);
  text-decoration: none;
}

@media (max-width: 576px) {
  .terms-content {
    margin-top: -40px;
  }

  .terms-panel {
    padding: 1.25rem;
  }
}
</style>
