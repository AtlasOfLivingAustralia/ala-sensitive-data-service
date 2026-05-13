
function Header() {
  return (
    <div id="wrapper-navbar" itemType="http://schema.org/WebSite">
      <a className="skip-link sr-only sr-only-focusable" href="#INSERT_CONTENT_ID_HERE">Skip to content</a>

      <nav className="navbar navbar-inverse navbar-expand-md">
        <div className="container-fluid header-logo-menu">
          <div className="navbar-header">
            <div>
              <a href="https://www.ala.org.au" className="custom-logo-link navbar-brand" itemProp="url">
                <img width="1005" height="150" src="https://www.ala.org.au/app/uploads/2019/01/logo.png"
                  className="custom-logo" alt="Atlas of Living Australia" itemProp="image"
                  srcSet="https://www.ala.org.au/app/uploads/2019/01/logo.png 1005w, https://www.ala.org.au/app/uploads/2019/01/logo-300x45.png 300w, https://www.ala.org.au/app/uploads/2019/01/logo-768x115.png 768w"
                  sizes="(max-width: 1005px) 100vw, 1005px" />
              </a>
            </div>
            <div className="display-flex signedIn">
              <button
                className="display-flex search-trigger hidden-md hidden-lg collapsed collapse-trigger-button"
                title="Open search dialog" data-toggle="collapse" data-target="#autocompleteSearchALA"
                onClick={() => { /* Implement focus logic in React if needed */ }}>
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="18" viewBox="0 0 22 22">
                  <defs>
                    {/* CORRECTED LINE: Use a string literal for the CSS content */}
                    <style>{`.search-icon { fill: #fff; fill-rule: evenodd; }`}</style>
                  </defs>
                  <path className="search-icon"
                    d="M1524.33,60v1.151a7.183,7.183,0,1,1-2.69.523,7.213,7.213,0,0,1,2.69-.523V60m0,0a8.333,8.333,0,1,0,7.72,5.217A8.323,8.323,0,0,0,1524.33,60h0Zm6.25,13.772-0.82.813,7.25,7.254a0.583,0.583,0,0,0,.82,0,0.583,0.583,0,0,0,0-.812l-7.25-7.254h0Zm-0.69-7.684,0.01,0c0-.006-0.01-0.012-0.01-0.018s-0.01-.015-0.01-0.024a6,6,0,0,0-7.75-3.3l-0.03.009-0.02.006v0a0.6,0.6,0,0,0-.29.293,0.585,0.585,0,0,0,.31.756,0.566,0.566,0,0,0,.41.01V63.83a4.858,4.858,0,0,1,6.32,2.688l0.01,0a0.559,0.559,0,0,0,.29.29,0.57,0.57,0,0,0,.75-0.305A0.534,0.534,0,0,0,1529.89,66.089Z"
                    transform="translate(-1516 -60)"></path>
                </svg>
                <span className="collapse visible-on-show" aria-hidden="true">&times;</span>
              </button>
              <a href="/login?path=https%3A%2F%2Fsds.ala.org.au%2F" role="button"
                className="account-mobile hidden-md hidden-lg loginBtn mobile-login-btn" title="Login button">
                <i className="fas fa-sign-in"></i>
              </a>
              <a href="https://auth.ala.org.au/userdetails/myprofile" role="button"
                className="account-mobile hidden-md hidden-lg myProfileBtn" title="Profile">
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="18" viewBox="0 0 37 41">
                  <defs><style>{`.account-icon { fill: #212121; fill-rule: evenodd; }`}</style></defs>
                  <path id="Account" className="account-icon"
                    d="M614.5,107.1a11.549,11.549,0,1,0-11.459-11.549A11.516,11.516,0,0,0,614.5,107.1Zm0-21.288a9.739,9.739,0,1,1-9.664,9.739A9.711,9.711,0,0,1,614.5,85.81Zm9.621,23.452H604.874a8.927,8.927,0,0,0-8.881,8.949V125h37v-6.785A8.925,8.925,0,0,0,624.118,109.262Zm7.084,13.924H597.789v-4.975a7.12,7.12,0,0,1,7.085-7.139h19.244a7.119,7.119,0,0,1,7.084,7.139v4.975Z"
                    transform="translate(-596 -84)"></path>
                </svg>
              </a>
              <a href="/logout?" role="button"
                className="account-mobile hidden-md hidden-lg logoutBtn mobile-logout-btn" title="Logout">
                <i className="fas fa-sign-out"></i>
              </a>
              <button className="navbar-toggle collapsed collapse-trigger-button" type="button"
                data-toggle="collapse" data-target="#navbarOuterWrapper" aria-controls="navbarOuterWrapper"
                aria-expanded="false" aria-label="Toggle navigation">
                <div className="horizontal-line"></div>
                <div className="horizontal-line"></div>
                <div className="horizontal-line"></div>
                <span className="collapse visible-on-show" aria-hidden="true">&times;</span>
              </button>
            </div>
          </div>

          <div id="navbarOuterWrapper" className="outer-nav-wrapper collapse navbar-collapse">
            <div className="top-bar hidden-xs hidden-sm">
              <a href="https://www.ala.org.au/contact-us/" className="btn btn-link btn-sm">Contact us</a>
              {/* <div className="account signedIn">
                <a href="https://auth.ala.org.au/userdetails/registration/createAccount"
                  className="btn btn-outline-white btn-sm signUpBtn" role="button">Sign up</a>
                <a href="https://auth.ala.org.au/userdetails/myprofile"
                  className="btn btn-outline-white btn-sm myProfileBtn" role="button">Profile</a>
                <a href="/login?path=https%3A%2F%2Fsds.ala.org.au%2F"
                  className="btn btn-primary btn-sm loginBtn">Login</a>
                <a href="/logout?" className="btn btn-outline-white btn-sm logoutBtn" role="button">Logout</a>
              </div> */}
            </div>
            <div className="main-nav-wrapper">
              <div id="navbarNavDropdown">
                <ul id="main-menu" className="nav navbar-nav" role="menubar">
                  <li itemType="https://www.schema.org/SiteNavigationElement"
                    className="menu-item menu-item-type-custom menu-item-object-custom menu-item-has-children dropdown nav-item show">
                    <a href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true"
                      className="dropdown-toggle nav-link" id="menu-item-dropdown-22">Search
                      &amp; analyse <span className="caret"></span></a>
                    <ul className="dropdown-menu" aria-labelledby="menu-item-dropdown-22" role="menu">
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-41958"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-41958 nav-item">
                        <a href="https://bie.ala.org.au/" className="dropdown-item">Search
                          species</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-23"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-23 nav-item">
                        <a href="https://biocache.ala.org.au/search#tab_simpleSearch"
                          className="dropdown-item">Search &amp; download records</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-28"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-28 nav-item">
                        <a href="https://collections.ala.org.au/datasets"
                          className="dropdown-item">Search datasets</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-41967"
                        role="separator" className="divider"></li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-24"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-24 nav-item">
                        <a href="https://spatial.ala.org.au/" className="dropdown-item">Spatial analysis
                          (Spatial Portal)</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-26"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-26 nav-item">
                        <a href="https://biocache.ala.org.au/explore/your-area"
                          className="dropdown-item">Explore
                          your area</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-31"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-31 nav-item">
                        <a href="https://collections.ala.org.au/" className="dropdown-item">Explore
                          natural
                          history collections</a>
                      </li>
                    </ul>
                  </li>
                  <li itemType="https://www.schema.org/SiteNavigationElement"
                    id="menu-item-32"
                    className="menu-item menu-item-type-custom menu-item-object-custom menu-item-has-children dropdown menu-item-32 nav-item">
                    <a href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
                      className="dropdown-toggle nav-link" id="menu-item-dropdown-32">Contribute
                      <span className="caret"></span></a>
                    <ul className="dropdown-menu" aria-labelledby="menu-item-dropdown-32" role="menu">
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-40773"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-40773 nav-item">
                        <a href="https://support.ala.org.au/support/solutions/articles/6000261427-sharing-a-dataset-with-the-ala"
                          className="dropdown-item">Share your dataset</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-40728"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-40728 nav-item">
                        <a href="https://lists.ala.org.au/public/speciesLists"
                          className="dropdown-item">Upload
                          species list</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-41968"
                        role="separator" className="divider"></li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-33"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-33 nav-item">
                        <a href="https://www.ala.org.au/home/record-a-sighting/"
                          className="dropdown-item">Record a sighting</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-35"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-35 nav-item">
                        <a href="https://volunteer.ala.org.au/" className="dropdown-item">Transcribe
                          &amp;
                          digitise (DigiVol)</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-37"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-37 nav-item">
                        <a href="https://biocollect.ala.org.au/acsa" className="dropdown-item">Discover
                          citizen science projects</a>
                      </li>
                    </ul>
                  </li>
                  <li itemType="https://www.schema.org/SiteNavigationElement"
                    className="menu-item menu-item-type-post_type menu-item-object-page menu-item-has-children dropdown nav-item">
                    <a href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
                      className="dropdown-toggle nav-link">Resources
                      <span className="caret"></span></a>
                    <ul className="dropdown-menu" aria-labelledby="menu-item-dropdown-199" role="menu">
                      <li itemType="https://www.schema.org/SiteNavigationElement"
                        className="menu-item menu-item-type-post_type menu-item-object-page nav-item">
                        <a href="https://www.ala.org.au/publications/"
                          className="dropdown-item">Brochures and reports</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement"
                        className="menu-item menu-item-type-post_type menu-item-object-page nav-item">
                        <a href="https://www.ala.org.au/ala-logo-and-identity/"
                          className="dropdown-item">ALA logo and identity</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement"
                        className="menu-item menu-item-type-post_type menu-item-object-page nav-item">
                        <a href="https://www.ala.org.au/ala-cited-publications/"
                          className="dropdown-item">ALA-cited publications</a>
                      </li>
                      <li className="menu-item menu-item-type-post_type menu-item-object-page nav-item"><a
                          href="https://www.ala.org.au/abdmp/" className="dropdown-item">Data
                          Mobilisation Program</a></li>
                      <li className="menu-item menu-item-type-post_type menu-item-object-page nav-item"><a
                          href="https://labs.ala.org.au/" className="dropdown-item">ALA Labs</a></li>
                    </ul>
                  </li>

                  <li itemType="https://www.schema.org/SiteNavigationElement"
                    id="menu-item-178"
                    className="menu-item menu-item-type-post_type menu-item-object-page menu-item-has-children dropdown menu-item-178 nav-item">
                    <a href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
                      className="dropdown-toggle nav-link" id="menu-item-dropdown-178">About
                      <span className="caret"></span></a>
                    <ul className="dropdown-menu" aria-labelledby="menu-item-dropdown-178" role="menu">
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-179"
                        className="menu-item menu-item-type-post_type menu-item-object-page menu-item-179 nav-item">
                        <a href="https://www.ala.org.au/about-ala/" className="dropdown-item">About
                          us</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-40734"
                        className="menu-item menu-item-type-post_type menu-item-object-page current_page_parent menu-item-40734 nav-item">
                        <a href="https://www.ala.org.au/governance"
                          className="dropdown-item">Governance</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-40734"
                        className="menu-item menu-item-type-post_type menu-item-object-page current_page_parent menu-item-40734 nav-item">
                        <a href="https://www.ala.org.au/blog/" className="dropdown-item">News &amp;
                          media</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-40734"
                        className="menu-item menu-item-type-post_type menu-item-object-page current_page_parent menu-item-40734 nav-item">
                        <a href="https://www.ala.org.au/careers/" className="dropdown-item">Careers</a>
                      </li>
                      <li
                        className="menu-item menu-item-type-post_type menu-item-object-page current_page_parent nav-item">
                        <a href="https://www.ala.org.au/internships/"
                          className="dropdown-item">Internships</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-175"
                        className="menu-item menu-item-type-post_type menu-item-object-page menu-item-175 nav-item">
                        <a href="https://www.ala.org.au/contact-us/" className="dropdown-item">Contact
                          us</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-41969"
                        role="separator" className="divider"></li>
                      <li itemType="https://www.schema.org/SiteNavigationElement"
                        className="menu-item menu-item-type-post_type menu-item-object-page nav-item">
                        <a href="https://www.ala.org.au/current-projects/"
                          className="dropdown-item">Current projects</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-40799"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-40731 nav-item">
                        <a href="https://living-atlases.gbif.org/"
                          className="dropdown-item">International Living Atlases</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-177"
                        className="menu-item menu-item-type-post_type menu-item-object-page menu-item-177 nav-item">
                        <a href="https://www.ala.org.au/indigenous-ecological-knowledge/"
                          className="dropdown-item">Indigenous ecological knowledge</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-41970"
                        role="separator" className="divider"></li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-41796"
                        className="menu-item menu-item-type-post_type menu-item-object-page menu-item-41796 nav-item">
                        <a href="https://www.ala.org.au/sites-and-services/"
                          className="dropdown-item">All
                          sites, services &amp; tools</a>
                      </li>
                    </ul>
                  </li>
                  <li itemType="https://www.schema.org/SiteNavigationElement"
                    id="menu-item-41391"
                    className="menu-item menu-item-type-custom menu-item-object-custom menu-item-has-children dropdown menu-item-41391 nav-item">
                    <a href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
                      className="dropdown-toggle nav-link" id="menu-item-dropdown-41391">Help
                      <span className="caret"></span></a>
                    <ul className="dropdown-menu" aria-labelledby="menu-item-dropdown-41391" role="menu">
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-41959"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-41959 nav-item">
                        <a href="https://support.ala.org.au/support/home"
                          className="dropdown-item">Browse
                          all articles (FAQs)</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-41960"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-41960 nav-item">
                        <a href="https://support.ala.org.au/support/solutions/6000137994"
                          className="dropdown-item">ALA Data help</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-41961"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-41961 nav-item">
                        <a href="https://support.ala.org.au/support/solutions/6000138053"
                          className="dropdown-item">ALA Tools &amp; Apps help</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-41962"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-41962 nav-item">
                        <a href="https://support.ala.org.au/support/solutions/6000138349"
                          className="dropdown-item">ALA Spatial Portal help</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement"
                        className="menu-item menu-item-type-custom menu-item-object-custom nav-item">
                        <a href="https://support.ala.org.au/support/solutions/articles/6000261662-citing-the-ala"
                          className="dropdown-item">How to cite the ALA</a>
                      </li>
                      <li itemType="https://www.schema.org/SiteNavigationElement" id="menu-item-41963"
                        className="menu-item menu-item-type-custom menu-item-object-custom menu-item-41963 nav-item">
                        <a href="https://www.ala.org.au/contact-us/" className="dropdown-item">Contact
                          us</a>
                      </li>
                    </ul>
                  </li>
                </ul>
              </div>
              <button className="search-trigger hidden-xs hidden-sm collapsed collapse-trigger-button"
                data-toggle="collapse" data-target="#autocompleteSearchALA"
                onClick={() => { /* Implement focus logic in React if needed */ }}>
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 22 22">
                  <title>Open search control</title>
                  <defs>
                    {/* CORRECTED LINE: Use a string literal for the CSS content */}
                    <style>{`.search-icon { fill: #fff; fill-rule: evenodd; }`}</style>
                  </defs>
                  <path className="search-icon"
                    d="M1524.33,60v1.151a7.183,7.183,0,1,1-2.69.523,7.213,7.213,0,0,1,2.69-.523V60m0,0a8.333,8.333,0,1,0,7.72,5.217A8.323,8.323,0,0,0,1524.33,60h0Zm6.25,13.772-0.82.813,7.25,7.254a0.583,0.583,0,0,0,.82,0,0.583,0.583,0,0,0,0-.812l-7.25-7.254h0Zm-0.69-7.684,0.01,0c0-.006-0.01-0.012-0.01-0.018s-0.01-.015-0.01-0.024a6,6,0,0,0-7.75-3.3l-0.03.009-0.02.006v0a0.6,0.6,0,0,0-.29.293,0.585,0.585,0,0,0,.31.756,0.566,0.566,0,0,0,.41.01V63.83a4.858,4.858,0,0,1,6.32,2.688l0.01,0a0.559,0.559,0,0,0,.29.29,0.57,0.57,0,0,0,.75-0.305A0.534,0.534,0,0,0,1529.89,66.089Z"
                    transform="translate(-1516 -60)"></path>
                </svg>
                <span className="collapse visible-on-show" aria-hidden="true" title="Close">&times;</span>
              </button>
            </div>

          </div>
        </div>
        <div className="container-fluid">
          <div id="autocompleteSearchALA" className="collapse">
            <form method="get" action="https://bie.ala.org.au/search" className="search-form">
              <div className="space-between">
                <input id="autocompleteHeader" type="text" name="q"
                  placeholder="Search species, datasets, and more..." className="search-input"
                  autoComplete="off" />
                <button className="search-submit" title="submit">
                  <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 22 22">
                    <defs>
                      {/* CORRECTED LINE: Use a string literal for the CSS content */}
                      <style>{`.search-icon { fill: #fff; fill-rule: evenodd; }`}</style>
                    </defs>
                    <path className="search-icon"
                      d="M1524.33,60v1.151a7.183,7.183,0,1,1-2.69.523,7.213,7.213,0,0,1,2.69-.523V60m0,0a8.333,8.333,0,1,0,7.72,5.217A8.323,8.323,0,0,0,1524.33,60h0Zm6.25,13.772-0.82.813,7.25,7.254a0.583,0.583,0,0,0,.82,0,0.583,0.583,0,0,0,0-.812l-7.25-7.254h0Zm-0.69-7.684,0.01,0c0-.006-0.01-0.012-0.01-0.018s-0.01-.015-0.01-0.024a6,6,0,0,0-7.75-3.3l-0.03.009-0.02.006v0a0.6,0.6,0,0,0-.29.293,0.585,0.585,0,0,0,.31.756,0.566,0.566,0,0,0,.41.01V63.83a4.858,4.858,0,0,1,6.32,2.688l0.01,0a0.559,0.559,0,0,0,.29.29,0.57,0.57,0,0,0,.75-0.305A0.534,0.534,0,0,0,1529.89,66.089Z"
                      transform="translate(-1516 -60)"></path>
                  </svg>
                </button>
              </div>
            </form>
          </div>
        </div>
      </nav>
    </div>
  );
}

export default Header;