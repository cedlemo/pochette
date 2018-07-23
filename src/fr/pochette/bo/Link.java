package fr.pochette.bo;

import java.time.LocalDate;

public class Link {
		int idLink;
		String title;
		String url;
		LocalDate creationDate;
		boolean consumed;
		LinkType linkType;
		
		/**
		 * @return the idLink
		 */
		public int getIdLink() {
			return idLink;
		}
		/**
		 * @param idLink the idLink to set
		 */
		public void setIdLink(int idLink) {
			this.idLink = idLink;
		}
		/**
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}
		/**
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}
		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}
		/**
		 * @param url the url to set
		 */
		public void setUrl(String url) {
			this.url = url;
		}
		/**
		 * @return the creationDate
		 */
		public LocalDate getCreationDate() {
			return creationDate;
		}
		/**
		 * @param creationDate the creationDate to set
		 */
		public void setCreationDate(LocalDate creationDate) {
			this.creationDate = creationDate;
		}
		/**
		 * @return the consumed
		 */
		public boolean isConsumed() {
			return consumed;
		}
		/**
		 * @param consumed the consumed to set
		 */
		public void setConsumed(boolean consumed) {
			this.consumed = consumed;
		}
		/**
		 * @return the linkType
		 */
		public LinkType getLinkType() {
			return linkType;
		}
		/**
		 * @param linkType the linkType to set
		 */
		public void setLinkType(LinkType linkType) {
			this.linkType = linkType;
		}
		/**
		 * @param idLink
		 * @param title
		 * @param url
		 * @param creationDate
		 * @param consumed
		 * @param linkType
		 */
		public Link(int idLink, String title, String url, LocalDate creationDate, boolean consumed, LinkType linkType) {
			super();
			this.setIdLink(idLink);
			this.setTitle(title);
			this.setUrl(url);
			this.setCreationDate(creationDate);
			this.setConsumed(consumed);
			this.setLinkType(linkType);
		}
		
		
		
}
