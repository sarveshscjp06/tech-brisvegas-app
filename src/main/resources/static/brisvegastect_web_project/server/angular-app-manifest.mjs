
export default {
  bootstrap: () => import('./main.server.mjs').then(m => m.default),
  inlineCriticalCss: true,
  baseHref: '/',
  locale: undefined,
  routes: [
  {
    "renderMode": 2,
    "redirectTo": "/home",
    "route": "/"
  },
  {
    "renderMode": 2,
    "route": "/home"
  },
  {
    "renderMode": 2,
    "route": "/about"
  },
  {
    "renderMode": 2,
    "route": "/how-it-works"
  },
  {
    "renderMode": 2,
    "route": "/services"
  },
  {
    "renderMode": 2,
    "route": "/contact"
  },
  {
    "renderMode": 2,
    "route": "/book-call"
  },
  {
    "renderMode": 2,
    "redirectTo": "/home",
    "route": "/**"
  }
],
  entryPointToBrowserMapping: undefined,
  assets: {
    'index.csr.html': {size: 2630, hash: '3b30fbc2b9849f428b551090f096fe51a4b480eaf40c8f7b27cdda27614e1579', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 961, hash: '7f4e5bdbc41b4f0a35ad8890d83de02795b9713cdd01b2f310ee0acb1a384e6d', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'services/index.html': {size: 12738, hash: '9f57480af09684d40f39ec98676dfc483bc550029d383dd26429600f9e09e2ac', text: () => import('./assets-chunks/services_index_html.mjs').then(m => m.default)},
    'how-it-works/index.html': {size: 12493, hash: '533705556792d08516e5a2a49c0a440ea7ab45ef13ad2156734394e91d3fc41e', text: () => import('./assets-chunks/how-it-works_index_html.mjs').then(m => m.default)},
    'about/index.html': {size: 13385, hash: 'f264abcf7ebef6ac5aaf2e6116bb9193ce337256b4f26202ec58618af4b3fbe3', text: () => import('./assets-chunks/about_index_html.mjs').then(m => m.default)},
    'home/index.html': {size: 11563, hash: '2431c105107667d7dbeb289de262ff6d600810c378b768ef0e2f406592e2a5db', text: () => import('./assets-chunks/home_index_html.mjs').then(m => m.default)},
    'contact/index.html': {size: 11868, hash: '6e5907d1ae33cf4df436fb883cb8c67bd08ab86b9595528ebc55fec09595b0f2', text: () => import('./assets-chunks/contact_index_html.mjs').then(m => m.default)},
    'book-call/index.html': {size: 13868, hash: 'b88396e0436fb57ff7598500032df083565b343e4fe944aca126db22967f495a', text: () => import('./assets-chunks/book-call_index_html.mjs').then(m => m.default)},
    'styles-VCIZX53D.css': {size: 5033, hash: 'QeIdGb00EXg', text: () => import('./assets-chunks/styles-VCIZX53D_css.mjs').then(m => m.default)}
  },
};
