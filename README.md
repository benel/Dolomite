DOLOMITE - Directories Led by Members
=====================================

License: [GNU Affero General Public License](http://www.gnu.org/licenses/agpl.html)

Contact: <aurelien.benel@utt.fr>

Home page: <http://dolomite.sf.net>

Installation and tests
----------------------

Install the [Java Development kit](http://www.oracle.com/technetwork/java/javase/downloads/).

Download [Play!](http://download.playframework.org/releases/play-1.1.1.zip) and unzip it somewhere without spaces in the full path.

In the "Play!" folder, clone the Git repository.

In "Dolomite/conf", copy "application.conf.sample" as "application.conf".

In the new file, edit (at least) the parameters about your LDAP directory:

    ldap.host = ldap.acme.com:19389
    ldap.dn = dc=acme,dc=com
    ldap.admin.dn = cn=admin,dc=acme,dc=com
    ldap.admin.password = sesame

Launch Dolomite in test mode:

    ./play test Dolomite

Open <http://localhost:9000/@tests> in a browser. If your LDAP settings are OK, you should be able to run "LdapTest".
    
If everything goes fine, define a community in your settings:

    ourcommunity.acme.com.name = Foo Bar
    ourcommunity.acme.com.href = http://ourapplication.acme.com/

Register the hostname ("ourcommunity.acme.com") as an alias for localhost in your local DNS settings file:

* /etc/hosts on Unices,
* C:\WINDOWS\system32\drivers\etc\hosts on Windows.

Run Dolomite:
    ./play run Dolomite

Open Dolomite at <http://ourcommunity.acme.com:9000/>
and invite someone by logging in with an existing LDAP account.

Run functional tests in <http://ourcommunity.acme.com:9000/@tests>


Production
----------

In the DNS settings of your domain, define the customized hostnames for your communities.

In "application.conf", add the corresponding settings For every community.

set application mode to "production":

    application.mode=prod

Define a real SMTP server (instead of the mock):

    mail.smtp.host = smtp.acme.com 

Go back to the "Play!" folder, and generate a new secret key for your instance:

    ./play secret Dolomite

Start Play! in the background:

    ./play start Dolomite

