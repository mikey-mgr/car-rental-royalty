const assert = require('assert');
const fs = require('fs');
const path = require('path');

const root = path.resolve(__dirname, '..');
const read = (relativePath) =>
  fs.readFileSync(path.join(root, relativePath), 'utf8');

const exists = (relativePath) =>
  fs.existsSync(path.join(root, relativePath));

const primaryPhone = '+263712768037';
const phoneDigits = '263712768037';
const addressLines = [
  '2870 Mainway Meadows',
  'Waterfalls, Harare',
];

assert(
  read('src/views/HomeView.vue').includes('<strong>Brian Langsoni</strong><br>CEO - DeRoyalty Car Rental'),
  'Homepage CEO signature should be Brian Langsoni'
);
assert(
  !read('src/views/HomeView.vue').includes('<strong>Braso</strong><br>CEO - DeRoyalty Car Rental'),
  'Homepage should no longer show Braso as CEO'
);

assert(exists('src/views/TermsAndConditions.vue'), 'Terms & Conditions page should exist');
const termsPage = read('src/views/TermsAndConditions.vue');
[
  'Terms and Conditions of Rental Agreement',
  '1. DEFINITIONS',
  '2. RISK, DELIVERY AND RETURN',
  '3. WARRANTIES BY YOU',
  '4. PAYMENTS',
  '5. LIABILITY WAIVERS',
  '6. EXTENSION OF RENTAL PERIOD',
  '7. TERMINATION',
  '8. INDEMNITY',
  '9. RESPONSIBILITY AFTER LOSS OR DAMAGE TO VEHICLE',
  '10. GENERAL',
  '10.16 The RENTER and/or DRIVER authorises US to conduct any credit checks',
].forEach((expectedText) => {
  assert(
    termsPage.includes(expectedText),
    `Terms page should include: ${expectedText}`
  );
});
addressLines.forEach((line) => {
  assert(termsPage.includes(line), `Terms page should include contact address line: ${line}`);
});
assert(termsPage.includes(primaryPhone), 'Terms page should include primary contact phone');

const termsScript = termsPage.match(/<script>([\s\S]*?)<\/script>/);
assert(termsScript, 'Terms page should include a script block');
const termsComponent = new Function(
  `${termsScript[1].replace('export default', 'return')}`
)();
assert.strictEqual(
  termsComponent.data().termsSections.length,
  10,
  'Terms page should render all 10 policy sections'
);

const router = read('src/router/index.js');
assert(router.includes('TermsAndConditions'), 'Router should register TermsAndConditions');
assert(router.includes("path: '/terms-and-conditions'"), 'Router should expose /terms-and-conditions');

const footer = read('src/components/Footer.vue');
assert(footer.includes("name: 'TermsAndConditions'"), 'Footer legal link should route to Terms & Conditions');
addressLines.forEach((line) => {
  assert(footer.includes(line), `Footer should include address line: ${line}`);
});
assert(footer.includes(primaryPhone), 'Footer should include primary contact phone');

const contactUs = read('src/views/ContactUs.vue');
addressLines.forEach((line) => {
  assert(contactUs.includes(line), `Contact page should include address line: ${line}`);
});
assert(contactUs.includes(primaryPhone), 'Contact page should include primary phone');

const app = read('src/App.vue');
assert(app.includes(`wa.me/${phoneDigits}`), 'WhatsApp floating button should use primary phone number');

const publicIndex = read('public/index.html');
assert(publicIndex.includes(`<meta name="contact" content="${primaryPhone}">`), 'SEO contact meta should use primary phone');
assert(publicIndex.includes(`"telephone": "${primaryPhone}"`), 'JSON-LD telephone should use primary phone');
assert(publicIndex.includes('"streetAddress": "2870 Mainway Meadows"'), 'JSON-LD should include street address');
assert(publicIndex.includes('"addressLocality": "Waterfalls, Harare"'), 'JSON-LD should include locality');
const jsonLd = publicIndex.match(/<script type="application\/ld\+json">([\s\S]*?)<\/script>/);
assert(jsonLd, 'public index should include JSON-LD');
const schema = JSON.parse(jsonLd[1]);
assert.strictEqual(schema.telephone, primaryPhone, 'Parsed JSON-LD telephone should use primary phone');
assert.strictEqual(schema.address.streetAddress, '2870 Mainway Meadows', 'Parsed JSON-LD should include street address');

const sitemap = read('public/sitemap.xml');
assert(sitemap.includes('/terms-and-conditions'), 'Sitemap should include Terms & Conditions route');

console.log('Content audit passed');
