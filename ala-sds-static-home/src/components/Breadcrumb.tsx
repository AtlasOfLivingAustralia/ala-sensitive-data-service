
function Breadcrumb() {
  return (
    <section id="breadcrumb">
      <div className="container">
        <div className="row">
          <nav aria-label="Breadcrumb" role="navigation">
            <ol className="breadcrumb-list">
              <li><a href="https://www.ala.org.au">Home</a></li>
              <li className="active">Sensitive Data Service</li>
            </ol>
          </nav>
        </div>
      </div>
    </section>
  );
}

export default Breadcrumb;